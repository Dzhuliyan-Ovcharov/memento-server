package com.memento.service;

import com.memento.model.Floor;

import java.util.Set;

public interface FloorService {

    Set<Floor> getAll();

    Floor save(Floor floor);

    Floor findByName(String name);
}
