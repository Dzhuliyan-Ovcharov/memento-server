package com.memento.service;

import com.memento.model.Neighborhood;

import java.util.List;

public interface NeighborhoodService {

    List<Neighborhood> findAllByCityName(String cityName);
}
