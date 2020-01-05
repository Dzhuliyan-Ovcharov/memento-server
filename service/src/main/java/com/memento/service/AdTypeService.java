package com.memento.service;

import com.memento.model.AdType;

import java.util.Set;

public interface AdTypeService {

    AdType findByType(String type);

    Set<AdType> getAll();

    AdType save(AdType adType);
}
