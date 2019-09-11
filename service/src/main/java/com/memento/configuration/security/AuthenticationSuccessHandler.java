package com.memento.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.memento.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenUtil jwtTokenUtil;
    private final ObjectMapper objectMapper;

    @Value("${jwt.expires_in:10000}")
    private int expires_in;

    @Autowired
    public AuthenticationSuccessHandler(final JwtTokenUtil jwtTokenUtil, final ObjectMapper objectMapper) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        clearAuthenticationAttributes(request);
        final User user = (User) authentication.getPrincipal();
        final String token = jwtTokenUtil.generateToken(user.getUsername(), user.getRole().getAuthority());
        final UserTokenState userTokenState = new UserTokenState(token, expires_in);
        final String jwtResponse = objectMapper.writeValueAsString(userTokenState);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(jwtResponse);
    }

    private static class UserTokenState {
        private final String access_token;
        private final int expires_in;

        public UserTokenState() {
            access_token = null;
            expires_in = 0;
        }

        public UserTokenState(final String access_token, final int expires_in) {
            this.access_token = access_token;
            this.expires_in = expires_in;
        }
    }
}
