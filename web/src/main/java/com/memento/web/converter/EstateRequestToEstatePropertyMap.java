package com.memento.web.converter;

import com.memento.model.Estate;
import com.memento.web.dto.EstateRequest;
import org.modelmapper.PropertyMap;

public class EstateRequestToEstatePropertyMap extends PropertyMap<EstateRequest, Estate> {

    @Override
    protected void configure() {
        map(source.getEmail(), destination.getUser().getEmail());
    }
}
