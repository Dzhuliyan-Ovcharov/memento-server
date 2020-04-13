package com.memento.web.converter;

import com.memento.model.Estate;
import com.memento.model.EstateFeature;
import com.memento.model.Image;
import com.memento.web.dto.EstateResponse;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EstateToEstateResponsePropertyMap extends PropertyMap<Estate, EstateResponse> {

    private static final Converter<Set<Image>, Set<String>> imageConverter = new ImageConverter();
    private static final Converter<Set<EstateFeature>, Set<String>> estateFeatureConverter = new EstateFeatureConverter();

    @Override
    protected void configure() {
        map(source.getUser().getFirstName(), destination.getFirstName());
        map(source.getUser().getLastName(), destination.getLastName());
        map(source.getUser().getEmail(), destination.getEmail());
        map(source.getFloor().getNumber(), destination.getFloor());
        using(imageConverter).map(source.getImages(), destination.getImages());
        using(estateFeatureConverter).map(source.getEstateFeatures(), destination.getFeatures());
    }

    private static class ImageConverter implements Converter<Set<Image>, Set<String>> {
        @Override
        public Set<String> convert(MappingContext<Set<Image>, Set<String>> context) {
            return context.getSource()
                    .stream()
                    .map(Image::getName)
                    .collect(Collectors.toSet());
        }
    }

    private static class EstateFeatureConverter implements Converter<Set<EstateFeature>, Set<String>> {
        @Override
        public Set<String> convert(MappingContext<Set<EstateFeature>, Set<String>> context) {
            return context.getSource()
                    .stream()
                    .map(EstateFeature::getFeature)
                    .collect(Collectors.toSet());
        }
    }
}
