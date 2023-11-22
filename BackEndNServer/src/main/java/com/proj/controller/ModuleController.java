// This handles http requests for modules from the front end

// Frontend -> ModuleController -> Backend

// Help Repository setup -> https://www.geeksforgeeks.org/spring-boot-crudrepository-with-example/
// Help JPARepositories annotation -> https://stackoverflow.com/questions/27856266/how-to-make-instance-of-crudrepository-interface-during-testing-in-spring 

package com.proj.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.proj.exception.FailedValidationException;
import com.proj.function.ModuleManager;
import com.proj.model.session.Module;

// The call hierarchy for the database connection is: Controller -> Manager -> Handler

@RestController
@RequestMapping(path = "/module") // TODO: User validation for any requests to "/module"
public class ModuleController {
    @Autowired
    ModuleManager moduleManager;

    // https://stackoverflow.com/questions/630453/what-is-the-difference-between-post-and-put-in-http
    @PutMapping(path = "/add")
    Module addModule(@RequestParam String name, @RequestParam String description, @RequestParam String levelRange) {
        try {
            Module addedModule = moduleManager.createModule(name, description, levelRange);
            return addedModule;
        } catch (FailedValidationException FVE) {
            // https://stackoverflow.com/questions/25422255/how-to-return-404-response-status-in-spring-boot-responsebody-method-return-t
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "validation failed");
        }
    }

    @GetMapping(path = "/getAll")
    ArrayList<Module> getModules() {
        Iterable<Module> modules = moduleManager.getModuledbHandler().findAll();

        ArrayList<Module> validModules = new ArrayList<Module>();

        for (Module module : modules) {
            if (module.getRemovedDate() == null) {
                validModules.add(module);
            }
        }
        return validModules;
    }

    // Only adds a removedDate, removing module from queries to the front end, while
    // keeping it in the database
    @DeleteMapping(path = "/removeByID")
    Module removeModule(@RequestParam String id) {
        Module moduleToRemove = moduleManager.getModuledbHandler().findById(Integer.valueOf(id));
        return moduleManager.removeModule(moduleToRemove);
    }

    @DeleteMapping(path = "/removeFromDatabase")
    void removeFromDatabase(@RequestParam String id) {
        Module moduleToRemove = moduleManager.getModuledbHandler().findById(Integer.valueOf(id));
        moduleManager.getModuledbHandler().delete(moduleToRemove);
    }

    @PutMapping(path = "/editByID")
    Module editModule(@RequestBody Module module) {
        Module editedModule = moduleManager.updateModule(module.getId(), module.getName(), module.getDescription(),
                module.getLevelRange());
        return editedModule;
    }
}
