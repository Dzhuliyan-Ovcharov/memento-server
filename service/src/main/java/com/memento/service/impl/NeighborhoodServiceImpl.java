package com.memento.service.impl;

import com.memento.model.Neighborhood;
import com.memento.repository.NeighborhoodRepository;
import com.memento.service.NeighborhoodService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Primary
@Log4j2
@Transactional
public class NeighborhoodServiceImpl implements NeighborhoodService {

    private final NeighborhoodRepository neighborhoodRepository;

    @Autowired
    public NeighborhoodServiceImpl(final NeighborhoodRepository neighborhoodRepository) {
        this.neighborhoodRepository = neighborhoodRepository;
    }

    @Override
    public List<Neighborhood> findAllByCityName(String cityName) {
        return neighborhoodRepository.findAllByCity_Name(cityName);
    }
}