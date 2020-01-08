package com.memento.web.converter;

import com.memento.model.Estate;
import com.memento.model.Image;
import com.memento.web.dto.EstateResponse;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;

import java.util.List;
import java.util.stream.Collectors;

public class EstateToEstateResponsePropertyMap extends PropertyMap<Estate, EstateResponse> {

    private static final Converter imageConverter = new ImageConverter();

    @Override
    protected void configure() {
        map(source.getUser().getFirstName(), destination.getFirstName());
        map(source.getUser().getLastName(), destination.getLastName());
        map(source.getUser().getEmail(), destination.getEmail());
        using(imageConverter).map(source.getImages(), destination.getImages());
    }

    private static class ImageConverter implements Converter<List<Image>, List<String>> {
        @Override
        public List<String> convert(MappingContext<List<Image>, List<String>> context) {
            return context.getSource()
                    .stream()
                    .map(Image::getName)
                    .collect(Collectors.toList());
        }
    }
}
