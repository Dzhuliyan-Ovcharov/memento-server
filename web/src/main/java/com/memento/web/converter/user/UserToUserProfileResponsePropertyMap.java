package com.memento.web.converter.user;

import com.memento.model.User;
import com.memento.web.dto.user.UserProfileResponse;
import org.modelmapper.PropertyMap;

public class UserToUserProfileResponsePropertyMap extends PropertyMap<User, UserProfileResponse> {

    @Override
    protected void configure() {
        map(source.getRole().getPermission(), destination.getPermission());
    }
}
