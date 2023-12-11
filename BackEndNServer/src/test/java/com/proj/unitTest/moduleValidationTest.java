package com.proj.unitTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Order;

import org.junit.jupiter.api.Test;

import com.proj.function.ModuleManager;
import com.proj.model.session.Module;

public class moduleValidationTest {
    @Test
    @Order(15)
    public void validateModuleIllegalChar() {
        Module module = new Module("name", "^\\I am bad description", "02;10");
        ModuleManager moduleManager = new ModuleManager();
        assertFalse(moduleManager.validateModule(module));
    }

    @Test
    @Order(16)
    public void validateModuleSuccess() {
        Module module = new Module("name", "I am good description", "01 to 15");
        ModuleManager moduleManager = new ModuleManager();
        assertTrue(moduleManager.validateModule(module));
    }

    @Test
    @Order(17)
    public void validateModuleNoName() {
        Module module = new Module("", "I am good description", "11 to 20");
        ModuleManager moduleManager = new ModuleManager();
        assertFalse(moduleManager.validateModule(module));
    }

    @Test
    @Order(17)
    public void validateModuleNameTooLong() {
        Module module = new Module("My name is very long; I have the longest name of a module and even by fantasy standards, it is simply too long", "I am good description", "19 to 20");
        ModuleManager moduleManager = new ModuleManager();
        assertFalse(moduleManager.validateModule(module));
    }

    @Test
    @Order(18)
    public void validateModuleLevelRanges() {
        Module module1 = new Module("NameIsANameIsAName", "I am good description", "15 to 15");
        Module module2 = new Module("NameIsANameIsAName", "I am good description", "11 to 01");
        Module module3 = new Module("NameIsANameIsAName", "I am good description", "00 to 01");
        Module module4 = new Module("NameIsANameIsAName", "I am good description", "20 to 21");
        ModuleManager moduleManager = new ModuleManager();

        assertTrue(moduleManager.validateModule(module1));
        assertFalse(moduleManager.validateModule(module2));
        assertFalse(moduleManager.validateModule(module3));
        assertFalse(moduleManager.validateModule(module4));
    }
}
