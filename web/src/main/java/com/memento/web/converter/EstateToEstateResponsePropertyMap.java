package com.memento.web.converter;

import com.memento.model.Estate;
import com.memento.web.dto.EstateResponse;
import org.modelmapper.PropertyMap;

public class EstateToEstateResponsePropertyMap extends PropertyMap<Estate, EstateResponse> {

    @Override
    protected void configure() {
        map(source.getUser().getFirstName(), destination.getFirstName());
        map(source.getUser().getLastName(), destination.getLastName());
        map(source.getUser().getEmail(), destination.getEmail());
    }
}
