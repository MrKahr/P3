package com.proj.function;

import org.springframework.beans.factory.annotation.Autowired;

import com.proj.model.session.Module;

public class ModuleManager { // TODO: Integration test TBA

    @Autowired
    private ModuleRepository moduleRepository;

    // Method

    /**
     * 
     * @param name
     * @param description
     * @param levelRange
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
     * 
     * @param module
     * @return
     */
    public Module removeModule(Module module) {
        // Remove module from database
        moduleRepository.delete(module); // deleteById(id): Delete an entity by its ID.
        return module;
    }

    /**
     * 
     * @param moduleID
     * @param name
     * @param description
     * @param levelRange
     * @return
     */
    public Module updateModule(Module module, Integer moduleID, String name, String description, String levelRange) {
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
        return moduleRepository.save(moduleToUpdate);
    }

    /**
     * 
     * @param module module to be validated
     * @return Boolean indicating whether validation was successful
     */
    public static Boolean validateModule(Module module) {
        if (module.getDescription().contains("\\") || module.getDescription().contains("^")) {// No backslashes or hat in description
            return false;
        } else if (module.getName().isEmpty()) {// Check that name is not empty
            return false;
        } else if (Integer.parseInt(module.getLevelRange()) > 0 && Integer.parseInt(module.getLevelRange()) <= 20) {
            // Max level from https://media.wizards.com/2016/dnd/downloads/DDADVL_FAQv3.pdf
            return false;
        } else {
            return true;
        }
    }
}
