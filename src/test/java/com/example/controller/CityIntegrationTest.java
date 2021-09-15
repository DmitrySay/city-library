package com.example.controller;

import com.example.dto.CityDto;
import com.example.model.City;
import com.example.repository.CityRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CityIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CityRepository cityRepository;
    private City city;

    @BeforeEach
    public void setUp() {
        city = City.builder()
                .id(1L).name("test name").photo("https://test-url.jpg")
                .build();
        cityRepository.save(city);
    }

    @WithMockUser(username = "admin_mock", roles = "ADMIN", password = "12356")
    @Test
    void getAll() throws Exception {
        String response = mockMvc.perform(get("/api/cities/")
                        .param("search", city.getName())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        JsonNode responseBody = objectMapper.readTree(response);
        JsonNode content = responseBody.path("content");
        List<CityDto> list = objectMapper.readValue(content.toString(), new TypeReference<>() {
        });

        assertTrue(list.size() > 0);
        CityDto cityDto = list.get(0);
        assertEquals(city.getId(), cityDto.getId());
        assertEquals(city.getName(), cityDto.getName());
        assertEquals(city.getPhoto(), cityDto.getPhoto());
    }
}