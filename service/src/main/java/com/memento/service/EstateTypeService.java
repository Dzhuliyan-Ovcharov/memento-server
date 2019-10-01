package com.memento.service;

import com.memento.model.EstateType;

import java.util.Set;

public interface EstateTypeService {

    Set<EstateType> getAll();

    EstateType save(EstateType estateType);

    EstateType update(EstateType estateType);
}
