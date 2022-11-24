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

import static com.example.security.SecurityPermission.AUTHORITY_ALL;
import static com.example.security.SecurityPermission.AUTHORITY_READ;
import static com.example.security.SecurityPermission.AUTHORITY_UPDATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CityIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CityRepository cityRepository;

    private City city;

    @BeforeEach
    public void setUp() {
        city = City.builder()
                .id(1L).name("city test name").photo("https://test-url.jpg")
                .build();
        cityRepository.save(city);
    }

    @Test
    void user_should_get_all_cities_by_name() throws Exception {
        String response = mockMvc.perform(get("/api/cities/")
                        .param("search", city.getName())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse().getContentAsString();

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

    // check PreAuthorize/hasAuthority
    @WithMockUser(username = "user_mock", authorities = AUTHORITY_ALL, password = "12356")
    @Test
    void user_with_authorities_all_should_update_city_and_get_ok() throws Exception {
        CityDto city = CityDto.builder()
                .id(1L).name("city update name").photo("https://update-test--update-url.jpg")
                .build();

        mockMvc.perform(put("/api/cities/{cityId}", "1")
                        .contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(city)
                        ))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void user_without_jwt_token_should_not_update_city_and_get_unauthorized() throws Exception {
        CityDto city = CityDto.builder()
                .id(1L).name("city test update name").photo("https://test--update-url.jpg")
                .build();

        mockMvc.perform(put("/api/cities/{cityId}", "1")
                        .contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(city)
                        ))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'error':'Full authentication is required to access this resource'}"))
                .andDo(print());
    }

    @WithMockUser(username = "user_mock", authorities = AUTHORITY_READ, password = "12356")
    @Test
    void user_with_authorities_read_should_not_update_city_and_get_forbidden() throws Exception {
        CityDto city = CityDto.builder()
                .id(1L).name("city test user update name").photo("https://user-test--update-url.jpg")
                .build();

        mockMvc.perform(put("/api/cities/{cityId}", "1")
                        .contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(city)
                        ))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'message':'Access is denied'}"))
                .andExpect(content().json("{'status': 403 }"))
                .andDo(print());
    }

    @WithMockUser(username = "user_mock", authorities = AUTHORITY_UPDATE, password = "12356")
    @Test
    void user_with_authorities_update_should_update_city_and_get_get_ok() throws Exception {
        CityDto city = CityDto.builder()
                .id(1L).name("city update name").photo("https://test--update-url.jpg")
                .build();

        mockMvc.perform(put("/api/cities/{cityId}", "1")
                        .contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(city)
                        ))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
