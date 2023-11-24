package com.proj.integrationTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;

import com.proj.function.ModuleManager;
import com.proj.model.session.Module;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// https://www.springcloud.io/post/2022-03/spring-boot-integration-testing-mysql-crud-rest-api-tutorial/#gsc.tab=0

// https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/context/SpringBootTest.WebEnvironment.html
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ModuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModuleManager moduleManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void requestAddModule() {
        try { /*
               * Add module with name "Name", description "desc is desc" and levelRange
               * "01-02" through mocked PUT request
               */
            ResultActions response = mockMvc
                    .perform(put("/module/add?name=Name&description=desc is desc&levelRange=01-02"));

            response.andExpect(status().isOk());

            //mockMvc.perform(delete("/module/removeFromDatabase?id=1"));
        } catch (Exception e) {
            fail("Unexpected exception thrown requestAddModule: " + e.getMessage()); // https://www.baeldung.com/junit-fail
        }
    }

    
    @Test
    public void requestEditModule() {
        try {
            moduleManager.createModule("Another Test Module", "It is indescribable.", "01-10");

            Module updateModule = new Module("New name", "New description", "02-05");
            updateModule.setId(1);

            ResultActions response = mockMvc.perform(put("/module/editByID")
                    .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateModule)));

            response.andExpect(status().isOk());
            moduleManager.getModuledbHandler().delete(updateModule);
        } catch (Exception e) {
            fail("Unexpected exception thrown by requestEditModule: " + e.getMessage());
        }
    }
    

    
    @Test
    public void requestRemoveModule() throws Exception {
        try {
            Module module = moduleManager.createModule("I live to die", "My fear is palpable", "20-20");
            assertTrue(moduleManager.getModuledbHandler().existsById(module.getId()));
            String request = "/module/removeByID?id=" + module.getId();

            ResultActions response = mockMvc.perform(delete(request));
            response.andExpect(status().isOk());

            ArrayList<Module> modules = new ArrayList<Module>();
            moduleManager.getModuledbHandler().findAll().forEach(modules::add);
            assertFalse(modules.contains(module));
        } catch (Exception e) {
            fail("Unexpected exception thrown by requestRemoveModule: " + e.getMessage());
        }
    }
    
}