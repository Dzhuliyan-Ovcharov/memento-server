package com.memento.web.converter;

import com.memento.model.Estate;
import com.memento.model.EstateFeature;
import com.memento.web.dto.EstateRequest;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;

import java.util.Set;
import java.util.stream.Collectors;

public class EstateRequestToEstatePropertyMap extends PropertyMap<EstateRequest, Estate> {

    private static final EstateFeatureConverter estateFeatureConverter = new EstateFeatureConverter();

    @Override
    protected void configure() {
        map(source.getFloor(), destination.getFloor().getNumber());
        map(source.getEmail(), destination.getUser().getEmail());
        using(estateFeatureConverter).map(source.getFeatures(), destination.getEstateFeatures());
    }


    private static class EstateFeatureConverter implements Converter<Set<String>, Set<EstateFeature>> {
        @Override
        public Set<EstateFeature> convert(MappingContext<Set<String>, Set<EstateFeature>> context) {
            return context.getSource()
                    .stream()
                    .map(feature -> EstateFeature.builder().feature(feature).build())
                    .collect(Collectors.toSet());
        }
    }
}
