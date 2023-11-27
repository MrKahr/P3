package com.proj.endToEndTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.proj.function.ModuleManager;
import com.proj.model.session.Module;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    public void requestAddModule() { // Add module through mocked PUT request
        try {
            String name = "The Circle of Life";
            String description = "I am going to change.";
            String levelRange = "01-02";
            ResultActions response = mockMvc
                    .perform(put("/module/add?name=" + name + "&description=" + description + "&levelRange=" + levelRange));

            response.andExpect(status().isOk());

            Module module = new Module(name, description, levelRange);
            module.setId(1);

            moduleManager.getModuledbHandler().delete(module);
        } catch (Exception e) {
            fail("Unexpected exception thrown requestAddModule: " + e.getMessage()); // https://www.baeldung.com/junit-fail
        }
    }

    @Test
    public void requestEditModule() { // Database persists from the last test, but not in the third
        try {
            moduleManager.createModule("null", "null", "01-02");

            Module updateModule = new Module("The Circle of Death", "I am not who I was.", "02-10");
            updateModule.setId(2);
            
            assertTrue(moduleManager.getModuledbHandler().existsById(2));

            ResultActions response = mockMvc.perform(put("/module/editByID")
                    .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateModule)));

            response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name",
                        is(updateModule.getName())))
                .andExpect(jsonPath("$.description",
                        is(updateModule.getDescription())))
                .andExpect(jsonPath("$.levelRange",
                        is(updateModule.getLevelRange())));
        } catch (Exception e) {
            fail("Unexpected exception thrown by requestEditModule: " + e.getMessage());
        }
    }
    
    @Test
    public void requestRemoveModule() {
        try {
            Module moduleToEdit = moduleManager.createModule("I live to die", "My fear is palpable", "20-20");

            ResultActions response = mockMvc.perform(delete("/module/removeByID?id=" + moduleToEdit.getId()));
            response.andExpect(status().isOk());

            assertTrue(moduleManager.getModuledbHandler().existsById(1));

        } catch (Exception e) {
            fail("Unexpected exception thrown by requestRemoveModule: " + e.getMessage());
        }
    }
}