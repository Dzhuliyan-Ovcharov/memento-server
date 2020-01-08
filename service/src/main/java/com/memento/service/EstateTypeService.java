package com.memento.service;

import com.memento.model.EstateType;

import java.util.Set;

public interface EstateTypeService {

    EstateType findByType(String type);

    Set<EstateType> getAll();

    EstateType save(EstateType estateType);

    EstateType update(EstateType estateType);
}
