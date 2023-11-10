package com.proj.function;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;

import com.proj.exception.FailedValidationException;
import com.proj.exception.InvalidInputException;
import com.proj.exception.NoModuleFoundException;
import com.proj.model.session.Module;

/**
 * ModuleManager is responsible for creating, removing, updating and validating the DnD modules.
 * - createModule: The method creates new modules with the string objects "name", "description" and "levelRange".
 *   After creating the module dato of creation will be add and the module is saved in the database.
 * - removeModule: The method takes the module and deletes it from the database. It add the dato of removal and saves it.
 * - updateModule: The method finds the module by ID and sets it to moduleUpdate. It checks if it can find the moduleID
 *   and turns a exception if it can't. The new values of the string objects within the module are the set.
 *   The module is then validated and saved if the validation fails an exception is thrown.
 * - validateModule: The method sets the min and max for the "levelRange" and then gets the "levelRange", "name" and "description".
 *   It then checks if description contains "\\" or "^" if present then exception is thrown. It then checks if name is empty and
 *   if it's too long exceptions are thrown if true. The "levelRange" is then checked if it's less than min or more than max.
 *   The max range is also checked if it's less than the min and if the max range is more than the max.
 *   If any is true than an exception is thrown. If the method runs through without exceptions it will return true.
 *   If the method catches any exceptions it will return false and the validation will fail.
 *   (PS levelRange isn't that same for every module it differentiate in each module)
 */
public class ModuleManager { // TODO: Integration test to be added later

    @Autowired
    private ModuleRepository moduleRepository;

    // Method

    /**
     * Creates module and saves it on database
     * 
     * @param name        of module
     * @param description of module
     * @param levelRange  of module
     * @return The added module
     */
    public Module createModule(String name, String description, String levelRange)
            throws IllegalArgumentException, FailedValidationException {
        Module module = new Module(name, description, levelRange);
        if (validateModule(module)) {
            // Add module to database
            module.setAddedDate(LocalDateTime.now());
            return moduleRepository.save(module);
        } else {
            throw new FailedValidationException("Validation failed");
        }
    }

    /**
     * Removes module from database
     * 
     * @param module to remove
     * @return removed module
     */
    public Module removeModule(Module module) throws IllegalArgumentException, OptimisticLockingFailureException {
        // Remove module from database
        moduleRepository.delete(module); // deleteById(id): Delete an entity by its ID.
        module.setRemovedDate(LocalDateTime.now());
        return module;
    }

    /**
     * Updates module in the database
     * 
     * @param moduleID    unique ID of module in database
     * @param name        of module
     * @param description of module
     * @param levelRange  of module
     * @return saved module
     */
    public Module updateModule(Integer id, String name, String description, String levelRange)
            throws NoModuleFoundException, FailedValidationException, IllegalArgumentException {
        Object moduleObject = moduleRepository.findById(id);
        Module moduleUpdate;
        if (moduleObject instanceof Module) { // If module is found we store it in moduleToUpdate, else we throw
            moduleUpdate = (Module) moduleObject;
        } else {
            throw new NoModuleFoundException("No module was found with the ID: " + id);
        }

        // Update module from database
        moduleUpdate.setName(name);
        moduleUpdate.setDescription(description);
        moduleUpdate.setLevelRange(levelRange);
        if (validateModule(moduleUpdate)) {
            return moduleRepository.save(moduleUpdate);
        } else {
            throw new FailedValidationException("The validation of module failed");
        }
    }

    /**
     * Validation for adding modules to database
     * Exception is logged in the console if an exception is thrown
     * 
     * @param module module to be validated
     * @return Boolean indicating whether validation was successful
     */
    public Boolean validateModule(Module module) {
        final int minLevel = 1, maxLevel = 20, maxNameLength = 30;
        String levelRangeMin = module.getLevelRange().substring(0, 2);
        String levelRangeMax = module.getLevelRange().substring(module.getLevelRange().length() - 2,
                module.getLevelRange().length());
        String name = module.getName();
        String description = module.getDescription();

        try {
            if (description.contains("\\") || module.getDescription().contains("^")) {
                // No backslashes or hat in description
                throw new InvalidInputException("Invalid character");
            } else if (name.isEmpty()) {
                throw new InvalidInputException("Empty name");
            } else if (name.length() > maxNameLength) {
                throw new InvalidInputException("Name too long");
            } else if (Integer.parseInt(levelRangeMin) < minLevel // Given min less than allowed min
                    || Integer.parseInt(levelRangeMin) > maxLevel // Given min greater than allowed max
                    || Integer.parseInt(levelRangeMax) < Integer.parseInt(levelRangeMin) // Given max is less than given
                                                                                         // min
                    || Integer.parseInt(levelRangeMax) > maxLevel) { // Given max is greater than allowed max
                // Max level from https://media.wizards.com/2016/dnd/downloads/DDADVL_FAQv3.pdf
                // Assumes the format starts and ends with a 2-digit number (can be 01, 02 etc)
                // TODO: check format for level range
                throw new InvalidInputException("Invalid level range");
            } else {
                return true;
            }
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
