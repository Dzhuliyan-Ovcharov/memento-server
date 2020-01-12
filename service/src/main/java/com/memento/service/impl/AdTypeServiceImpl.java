package com.memento.service.impl;

import com.memento.model.AdType;
import com.memento.repository.AdTypeRepository;
import com.memento.service.AdTypeService;
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
public class AdTypeServiceImpl implements AdTypeService {

    private final AdTypeRepository adTypeRepository;

    @Autowired
    public AdTypeServiceImpl(final AdTypeRepository adTypeRepository) {
        this.adTypeRepository = adTypeRepository;
    }

    @Override
    public AdType findByType(@NonNull final String type) {
        return adTypeRepository.findAdTypeByType(type)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public Set<AdType> getAll() {
        return Set.copyOf(adTypeRepository.findAll());
    }

    @Override
    @Transactional
    public AdType save(@NonNull final AdType adType) {
        return adTypeRepository.save(adType);
    }
}
