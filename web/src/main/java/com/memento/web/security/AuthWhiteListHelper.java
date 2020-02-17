package com.memento.web.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

@Getter
@Component
public class AuthWhiteListHelper {

    private final Set<String> authWhiteList;

    public AuthWhiteListHelper(@Value(value = "#{'${auth.white.list}'.trim().split('\\s*,\\s*')}") final Set<String> authWhiteList) {
        this.authWhiteList = authWhiteList;
    }

    public boolean anyMatch(String uri) {
        return authWhiteList.contains(uri);
    }

}
