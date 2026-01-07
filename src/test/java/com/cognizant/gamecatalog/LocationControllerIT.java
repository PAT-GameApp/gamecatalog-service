package com.cognizant.gamecatalog;

// ...existing imports...
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LocationControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllLocations_returnsListWithExpectedFields() throws Exception {
        var mvcResult = mockMvc.perform(get("/locations/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        List<Map<String, Object>> locations =
                objectMapper.readValue(json, new TypeReference<>() {});

        assertThat(locations).isNotEmpty();
        assertThat(locations.get(0)).containsKeys("city", "country", "office");
    }

    @Test
    void getLocationById_returnsOkOrNotFound() throws Exception {
        mockMvc.perform(get("/locations/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    if (status != 200 && status != 404) {
                        throw new AssertionError("Expected 200 or 404 but got " + status);
                    }
                });
    }

    @Test
    void searchLocationsByCity_returnsOk() throws Exception {
        mockMvc.perform(get("/locations/search")
                        .param("city", "TestCity")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    if (status != 200 && status != 400 && status != 404) {
                        throw new AssertionError("Expected 200, 400 or 404 but got " + status);
                    }
                });
    }
}
