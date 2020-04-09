package com.memento.service.impl;

import com.memento.model.EstateFeature;
import com.memento.repository.EstateFeatureRepository;
import com.memento.service.EstateFeatureService;
import com.memento.shared.exception.ResourceNotFoundException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Primary
@Slf4j
@Transactional
public class EstateFeatureServiceImpl implements EstateFeatureService {

    private final EstateFeatureRepository estateFeatureRepository;

    @Autowired
    public EstateFeatureServiceImpl(final EstateFeatureRepository estateFeatureRepository) {
        this.estateFeatureRepository = estateFeatureRepository;
    }

    @Override
    public Set<EstateFeature> getAll() {
        return Set.copyOf(estateFeatureRepository.findAll());
    }

    @Override
    public Set<EstateFeature> findByFeatures(Set<String> features) {
        return Set.copyOf(estateFeatureRepository.findAllByFeatureIn(features));
    }

    @Override
    public EstateFeature save(@NonNull final EstateFeature estateFeature) {
        return estateFeatureRepository.save(estateFeature);
    }

    @Override
    public EstateFeature findByFeature(@NonNull final String feature) {
        return estateFeatureRepository.findEstateFeatureByFeature(feature)
                .orElseThrow(ResourceNotFoundException::new);
    }
}
