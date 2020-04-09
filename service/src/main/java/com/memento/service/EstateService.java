package com.memento.service;

import com.memento.model.Estate;
import com.memento.model.User;

import java.util.Set;

public interface EstateService {

    Estate findById(Long id);

    Set<Estate> getAll();

    Estate save(Estate estate);

    Estate update(Long id, Estate estate);

    Set<Estate> getEstatesByUserEmail(String email);
}
