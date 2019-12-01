package com.memento.service.impl;

import com.memento.model.Estate;
import com.memento.repository.EstateRepository;
import com.memento.service.EstateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Primary
@Slf4j
@Transactional
public class EstateServiceImpl implements EstateService {

    private final EstateRepository estateRepository;

    @Autowired
    public EstateServiceImpl(final EstateRepository estateRepository) {
        this.estateRepository = estateRepository;
    }

    @Override
    public Estate save(Estate estate) {
        return estateRepository.save(estate);
    }
}
