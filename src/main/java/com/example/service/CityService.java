package com.example.service;

import com.example.dto.CityDto;
import com.example.mapper.CityMapper;
import com.example.model.City;
import com.example.repository.CityRepository;
import com.example.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    @Transactional(readOnly = true)
    public City getById(Long cityId) {
        return cityRepository.findById(cityId)
                .orElseThrow(() -> {
                    log.warn("City with id = {} is not found", cityId);
                    return new NotFoundException("City is not found.");
                });
    }

    @Transactional(readOnly = true)
    public Page<CityDto> getAll(String search, Pageable pageable) {
        Page<City> page;
        if (StringUtils.hasLength(search)) {
            page = cityRepository.findAllByName(search, pageable);
        } else {
            page = cityRepository.findAll(pageable);
        }
        return page.map(cityMapper::toDto);
    }

    @Transactional
    public CityDto update(Long cityId, CityDto cityDto) {
        cityDto.setId(cityId);
        City city = this.getById(cityId);
        cityMapper.updateEntityFromDto(cityDto, city);
        return cityMapper.toDto(cityRepository.save(city));
    }
}