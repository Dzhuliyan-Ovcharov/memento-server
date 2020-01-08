package com.memento.web.converter;

import com.memento.model.User;
import com.memento.web.dto.UserRegisterRequest;
import org.modelmapper.PropertyMap;

public class UserRegisterRequestToUserPropertyMap extends PropertyMap<UserRegisterRequest, User> {

    @Override
    protected void configure() {
        map(source.getPermission(), destination.getRole().getPermission());
    }
}
