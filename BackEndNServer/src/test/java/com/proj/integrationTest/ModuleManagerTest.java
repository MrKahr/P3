package com.proj.integrationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.proj.function.ModuleManager;
import com.proj.model.session.Module;

@SpringBootTest
public class ModuleManagerTest {

    @Autowired
    ModuleManager moduleManager;

    @Test
    public void updateAndShowModule() {
        Module module = moduleManager.createModule("name", "desc", "01-02");
        
        Module foundModule = moduleManager.updateModule(module.getId(), "NewName", "NewDesc", "02-03");
        assertEquals(foundModule.getName(), "NewName");
        moduleManager.getModuledbHandler().delete(foundModule);
    }

    @Test
    public void removeModuleFromDatabase() {
        Module module = moduleManager.createModule("Cool Cold Adventure", "In the icy depths of cold.\n You will freeze.", "01-02");
        moduleManager.getModuledbHandler().delete(module);
        assertFalse(moduleManager.getModuledbHandler().existsById(module.getId()));
    }

    @Test
    public void setModuleRemovedDate() {
        Module module = moduleManager.createModule("null adventure", "null description", "05 to 10");
        moduleManager.removeModule(module);
        Module updatedModule = moduleManager.getModuledbHandler().findById(module.getId());

        assertTrue(updatedModule.getRemovedDate() != null);

        moduleManager.getModuledbHandler().delete(updatedModule);
    }
}
