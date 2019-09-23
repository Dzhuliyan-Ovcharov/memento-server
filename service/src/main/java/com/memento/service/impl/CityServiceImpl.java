package com.memento.service.impl;

import com.memento.model.City;
import com.memento.model.Neighborhood;
import com.memento.repository.CityRepository;
import com.memento.service.CityService;
import com.memento.shared.exception.ResourceNotFoundException;
import io.jsonwebtoken.lang.Collections;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Primary
@Log4j2
@Transactional
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public Set<City> getAll() {
        return Set.copyOf(cityRepository.findAll());
    }

    @Override
    public City save(City city) {
        return cityRepository.save(city);
    }

    @Override
    public City update(City city) {
        City oldCity = cityRepository.findById(city.getId()).orElseThrow(ResourceNotFoundException::new);
        oldCity.getNeighborhoods().clear();

        final City newCity = City.builder()
                .id(oldCity.getId())
                .name(city.getName())
                .neighborhoods(city.getNeighborhoods())
                .build();

        return cityRepository.save(newCity);
    }

    private Neighborhood buildNeighborhood(Neighborhood neighborhood, City city) {
        return Neighborhood.builder()
                .id(neighborhood.getId())
                .name(neighborhood.getName())
                .city(city)
                .build();
    }
}
