package com.memento.service.impl;

import com.memento.model.AdType;
import com.memento.repository.AdTypeRepository;
import com.memento.service.AdTypeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Primary
@Log4j2
@Transactional
public class AdTypeServiceImpl implements AdTypeService {

    private final AdTypeRepository adTypeRepository;

    @Autowired
    public AdTypeServiceImpl(final AdTypeRepository adTypeRepository) {
        this.adTypeRepository = adTypeRepository;
    }

    @Override
    public Set<AdType> getAll() {
        return Set.copyOf(adTypeRepository.findAll());
    }

    @Override
    public AdType save(AdType adType) {
        return adTypeRepository.save(adType);
    }
}
