package com.memento.service.impl;

import com.memento.model.City;
import com.memento.repository.CityRepository;
import com.memento.service.CityService;
import com.memento.shared.exception.BadRequestException;
import com.memento.shared.exception.ResourceNotFoundException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Set;

@Service
@Primary
@Slf4j
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Autowired
    public CityServiceImpl(final CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public Set<City> getAll() {
        return Set.copyOf(cityRepository.findAll());
    }

    @Override
    @Transactional
    public City save(@NonNull City city) {
        return cityRepository.save(city);
    }

    @Override
    @Transactional
    public City update(@NonNull Long id, @NonNull City city) {
        if(!id.equals(city.getId())) {
            throw new BadRequestException(String.format("The ids does not match. First id is %d. Second id is %d", id, city.getId()));
        }

        final City oldCity = findById(id);
        oldCity.getNeighborhoods().clear();

        final City newCity = City.builder()
                .id(oldCity.getId())
                .name(city.getName())
                .neighborhoods(city.getNeighborhoods())
                .build();

        return cityRepository.save(newCity);
    }

    @Override
    public City findById(@NonNull Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("City with id %d does not exists", id)));
    }
}
