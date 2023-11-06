package com.proj.function;

import org.springframework.beans.factory.annotation.Autowired;

import com.proj.Exceptions.InvalidInputException;
import com.proj.model.session.Module;

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
    public Module createModule(String name, String description, String levelRange) {
        Module module = new Module(name, description, levelRange);
        if (validateModule(module)) {
            // Add module to database
            moduleRepository.save(module);
            return module;
        } else {
            return null; // Maybe throw validation exception
        }
    }

    /**
     * Removes module from database
     * 
     * @param module to remove
     * @return removed module
     */
    public Module removeModule(Module module) {
        // Remove module from database
        moduleRepository.delete(module); // deleteById(id): Delete an entity by its ID.
        return module;
    }

    /**
     * Updates module in the database
     * @param moduleID    unique ID of module in database
     * @param name        of module
     * @param description of module
     * @param levelRange  of module
     * @return saved module
     */
    public Module updateModule(Integer moduleID, String name, String description, String levelRange) {
        Object moduleObject = moduleRepository.findById(moduleID);
        Module moduleToUpdate;
        if (moduleObject instanceof Module) { // If module is found we store it in moduleToUpdate, else we store null
            moduleToUpdate = (Module) moduleObject;
        } else {
            moduleToUpdate = null;
        }

        // Update module from databasee
        moduleToUpdate.setName(name);
        moduleToUpdate.setDescription(description);
        moduleToUpdate.setLevelRange(levelRange);
        if (validateModule(moduleToUpdate)) {
            return moduleRepository.save(moduleToUpdate);
        } else {
            return null;
        }
    }

    /**
     * Validation for adding modules to database
     * Exception is logged in the console if an exception is thrown
     * @param module module to be validated
     * @return Boolean indicating whether validation was successful
     */
    public Boolean validateModule(Module module) throws InvalidInputException {
        int minLevel = 1, maxLevel = 20;
        String levelRange = module.getLevelRange();
        String name = module.getName();
        String description = module.getDescription();
        try {
            if (description.contains("\\") || module.getDescription().contains("^")) {
                // No backslashes or hat in description
                throw new InvalidInputException("Invalid character");
            } else if (name.isEmpty()) {// Check that name is not empty
                throw new InvalidInputException("Empty name");
            } else if (Integer.parseInt(levelRange.substring(0,2)) < minLevel
                    || Integer.parseInt(levelRange.substring(levelRange.length() - 2), levelRange.length()) > maxLevel) {
                // Max level from https://media.wizards.com/2016/dnd/downloads/DDADVL_FAQv3.pdf
                // Assumes the format starts and ends with a 2-digit number (can be 01, 02 etc)
                //TODO: check format for level range
                throw new InvalidInputException("Invalid level range");
            } else {
                return true;
            }
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }
}
