package com.proj.integrationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.proj.repositoryhandler.ModuledbHandler;
import com.proj.model.session.Module;

@SpringBootTest
public class ModuledbHandlerTest {

    @Autowired
    private ModuledbHandler moduledbHandler;

    @Test
    @Order(90)
    public void createAndRetrieveModule() {
        Module module = new Module("name", "desc", "01-02");

        moduledbHandler.save(module);
        Module foundModule = moduledbHandler.findById(module.getId());
        
        //equals on objects is only true if it is the same object, not if all fields are the same
        assertEquals(module.getId(), foundModule.getId());
        // Test data is deleted from database after the test, to avoid issues in "production"
        moduledbHandler.delete(module);
    }
}
