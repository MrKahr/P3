package com.proj.unitTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.proj.function.ModuleManager;
import com.proj.model.session.Module;

public class moduleValidationTest {
    @Test
    public void validateModuleIllegalChar() {
        Module module = new Module("name", "^\\I am bad description", "02;10");
        ModuleManager moduleManager = new ModuleManager();
        assertFalse(moduleManager.validateModule(module));
    }

    @Test
    public void validateModuleSuccess() {
        Module module = new Module("name", "I am good description", "01 to 15");
        ModuleManager moduleManager = new ModuleManager();
        assertTrue(moduleManager.validateModule(module));
    }

    @Test
    public void validateModuleNoName() {
        Module module = new Module("", "I am good description", "11 to 20");
        ModuleManager moduleManager = new ModuleManager();
        assertFalse(moduleManager.validateModule(module));
    }
}
