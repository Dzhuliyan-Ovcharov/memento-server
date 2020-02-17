package com.memento.web.security;

import com.memento.model.User;
import com.memento.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final AuthWhiteListHelper authWhiteListHelper;

    @Autowired
    public JwtRequestFilter(final JwtTokenUtil jwtTokenUtil,
                            final UserService userService,
                            final AuthWhiteListHelper authWhiteListHelper) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.authWhiteListHelper = authWhiteListHelper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return authWhiteListHelper.anyMatch(request.getServletPath());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = jwtTokenUtil.getTokenFromRequest(request);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(null, null);
        if (Strings.isNotEmpty(token)) {
            try {
                String email = jwtTokenUtil.getEmailFromToken(token);

                if (Strings.isNotEmpty(email)) {
                    final User user = userService.findByEmail(email);
                    usernamePasswordAuthenticationToken = validateToken(token, user, request);
                }

            } catch (Exception ex) {
                log.error("Unable to get JWT.", ex);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken validateToken(String token, User user, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken toReturn = new UsernamePasswordAuthenticationToken(null, null);

        if (jwtTokenUtil.validateToken(token, user)) {
            toReturn = new UsernamePasswordAuthenticationToken(user, user.getPassword(), Set.of(user.getRole()));
            toReturn.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            return toReturn;
        }

        return toReturn;
    }
}
