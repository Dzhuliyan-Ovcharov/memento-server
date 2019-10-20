package com.memento.service.configuration.security;

import com.memento.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    @Autowired
    public JwtRequestFilter(final JwtTokenUtil jwtTokenUtil, final UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = jwtTokenUtil.getTokenFromRequest(request);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(null, null);
        if (Strings.isNotEmpty(token)) {
            try {
                String username = jwtTokenUtil.getUsernameFromToken(token);

                if (Strings.isNotEmpty(username)) {
                    final UserDetails user = userService.loadUserByUsername(username);
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

    private UsernamePasswordAuthenticationToken validateToken(String token, UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken toReturn = new UsernamePasswordAuthenticationToken(null, null);

        if (jwtTokenUtil.validateToken(token, userDetails)) {
            toReturn = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
            toReturn.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            return toReturn;
        }

        return toReturn;
    }
}
