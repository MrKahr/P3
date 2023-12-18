// This handles http requests for modules from the front end

// Frontend -> ModuleController -> Backend

// Help Repository setup -> https://www.geeksforgeeks.org/spring-boot-crudrepository-with-example/
// Help JPARepositories annotation -> https://stackoverflow.com/questions/27856266/how-to-make-instance-of-crudrepository-interface-during-testing-in-spring 

package com.proj.controller.navigation;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proj.function.ModuleManager;
import com.proj.model.session.Module;

// The call hierarchy for the database connection is: Controller -> Manager -> Handler

@RestController
public class ModuleController {
    @Autowired
    private ModuleManager moduleManager;

    // https://stackoverflow.com/questions/630453/what-is-the-difference-between-post-and-put-in-http
    @PutMapping(path = "/admin/module/add")
    Module addModule(@RequestParam String name, @RequestParam String description, @RequestParam String levelRange) {
        Module addedModule = moduleManager.createModule(name, description, levelRange);
        return addedModule;
    }

    @GetMapping(path = {"/dm/module/getAll", "/admin/module/getAll"})
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
    @DeleteMapping(path = "/admin/module/removeByID")
    Module removeModule(@RequestParam String id) {
        Module moduleToRemove = moduleManager.getModuledbHandler().findById(Integer.valueOf(id));
        return moduleManager.removeModule(moduleToRemove);
    }

    @DeleteMapping(path = "/admin/module/removeFromDatabase")
    void removeFromDatabase(@RequestParam String id) {
        Module moduleToRemove = moduleManager.getModuledbHandler().findById(Integer.valueOf(id));
        moduleManager.getModuledbHandler().delete(moduleToRemove);
    }

    @PutMapping(path = "/admin/module/editByID")
    Module editModule(@RequestBody Module module) {
        Module editedModule = moduleManager.updateModule(module.getId(), module.getName(), module.getDescription(),
                module.getLevelRange());
        return editedModule;
    }
}
