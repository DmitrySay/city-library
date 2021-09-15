package com.example.service;

import com.example.controller.BaseIntegrationTest;
import com.example.dto.CityDto;
import com.example.mapper.CityMapper;
import com.example.model.City;
import com.example.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CityServiceTest extends BaseIntegrationTest {
    private CityRepository cityRepository;
    private CityService cityService;

    @Autowired
    private CityMapper cityMapper;

    @BeforeEach
    void setUp() {
        cityRepository = mock(CityRepository.class);
        cityService = new CityService(cityRepository, cityMapper);
    }

    @Test
    void getById() {
        Long id = 1L;
        City city = City.builder()
                .id(1L).name("fake name").photo("https://test-url.jpg").build();
        Optional<City> mockCity = Optional.of(city);
        doReturn(mockCity).when(cityRepository).findById(id);
        assertEquals(city, cityService.getById(id));
    }

    @Test
    void update() {
        Long id = 1L;
        CityDto cityUpdateDto = CityDto.builder()
                .id(1L).name("update fake name").photo("https://test-update-url.jpg").build();
        City city = City.builder()
                .id(1L).name("fake name").photo("https://test-url.jpg").build();
        Optional<City> mockCity = Optional.of(city);

        doReturn(mockCity).when(cityRepository).findById(id);
        doReturn(mockCity.get()).when(cityRepository).save(any(City.class));

        CityDto updatedDto = cityService.update(id, cityUpdateDto);
        verify(cityRepository, times(1)).save(any(City.class));
        assertEquals(cityUpdateDto, updatedDto);
    }
}

