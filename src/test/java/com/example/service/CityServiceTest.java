package com.example.service;

import com.example.mapper.CityMapper;
import com.example.model.City;
import com.example.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {
    private CityRepository cityRepository;
    private CityService cityService;
    private CityMapper cityMapper;

    @BeforeEach
    void setUp() {
        cityRepository = mock(CityRepository.class);
        cityMapper = mock(CityMapper.class);
        cityService = new CityService(cityRepository, cityMapper);
    }

    @Test
    void getById() {
        Long id = 1L;
        City city = City.builder().id(1L).name("fake name").photo("https://test-url.jpg").build();
        Optional<City> mockCity = Optional.of(city);
        doReturn(mockCity).when(cityRepository).findById(id);
        assertEquals(city, cityService.getById(id));
    }
}
