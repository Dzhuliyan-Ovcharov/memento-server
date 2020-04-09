package com.memento.service;

import com.memento.model.EstateFeature;

import java.util.Set;

public interface EstateFeatureService {

    Set<EstateFeature> getAll();

    Set<EstateFeature> findByFeatures(Set<String> features);

    EstateFeature save(EstateFeature estateFeature);

    EstateFeature findByFeature(String feature);
}
