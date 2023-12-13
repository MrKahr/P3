package com.proj.endToEndTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
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
@AutoConfigureMockMvc(addFilters = false) // MockMvc allows us to mock HTTP requests to our REST controllers
// "addFilters = false" disables Spring Security in test
public class ModuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModuleManager moduleManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(110)
    // Add module through mocked PUT request to test controller
    public void requestAddModule() {
        try {
            Module module = new Module ("Creation", "I am going to born.", "01-01");
            module.setId(1);
            ResultActions response = mockMvc
                    .perform(put("/admin/module/add?name=" + module.getName() + "&description=" + module.getDescription() + "&levelRange=" + module.getLevelRange()));

            response.andExpect(status().isOk());

            moduleManager.getModuledbHandler().delete(module); // Cleanup
        } catch (Exception e) {
            fail("Unexpected exception thrown requestAddModule: " + e.getMessage()); // https://www.baeldung.com/junit-fail
        }
    }

    @Test
    @Order(111)
    // Edit module through mocked PUT request to test controller
    public void requestEditModule() { // Database persists from the last test, but not in the third
        try {
            moduleManager.createModule("The Circle of Life", "I am going to change.", "01-02");

            Module updateModule = new Module("The Circle of Death", "I am not who I was.", "02-10");
            updateModule.setId(2);
            
            assertTrue(moduleManager.getModuledbHandler().existsById(2));

            ResultActions response = mockMvc.perform(put("/admin/module/editByID") // Compare fields of response to expected values
                    .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateModule)));

            response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name",
                        is(updateModule.getName())))
                .andExpect(jsonPath("$.description",
                        is(updateModule.getDescription())))
                .andExpect(jsonPath("$.levelRange",
                        is(updateModule.getLevelRange())));
            moduleManager.getModuledbHandler().delete(updateModule); // Cleanup
        } catch (Exception e) {
            fail("Unexpected exception thrown by requestEditModule: " + e.getMessage());
        }
    }
    
    @Test
    @Order(112)
    // Delete module through mocked PUT request to test controller
    public void requestRemoveModule() {
        try {
            Module moduleToRemove = moduleManager.createModule("I live to die", "My fear is palpable", "20-20");

            ResultActions response = mockMvc.perform(delete("/admin/module/removeByID?id=" + moduleToRemove.getId()));
            response.andExpect(status().isOk());

            assertTrue(moduleManager.getModuledbHandler().existsById(1));

            moduleManager.getModuledbHandler().delete(moduleToRemove); // Cleanup
        } catch (Exception e) {
            fail("Unexpected exception thrown by requestRemoveModule: " + e.getMessage());
        }
    }
}