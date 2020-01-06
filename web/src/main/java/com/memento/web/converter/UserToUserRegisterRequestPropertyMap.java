package com.memento.web.converter;

import com.memento.model.User;
import com.memento.web.dto.UserRegisterRequest;
import org.modelmapper.PropertyMap;

public class UserToUserRegisterRequestPropertyMap extends PropertyMap<User, UserRegisterRequest> {

    @Override
    protected void configure() {
        map(source.getRole().getPermission(), destination.getPermission());
    }
}
