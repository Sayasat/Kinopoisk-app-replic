package com.sayasat.exp2.kinopoisk_check.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.sayasat.exp2.kinopoisk_check.services.UserDetailsImplService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserDetailsImplService userDetailsImplService;

    public JWTFilter(JWTUtil jwtUtil, UserDetailsImplService userDetailsImplService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsImplService = userDetailsImplService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = httpServletRequest.getHeader("Authorization");

        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            if(jwt.isBlank()) {
                httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid JWT Token in Bearer Header");
            }else {
                try {
                    String username = jwtUtil.validateTokenAndRetrieveClaim(jwt);
                    String role = jwtUtil.getRoleFromToken(jwt);

                    UserDetails userDetails = userDetailsImplService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails,
                                    userDetails.getPassword(),
                                    Collections.singletonList(new SimpleGrantedAuthority(role)));

                    if(SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (JWTVerificationException e) {
                    httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid JWT Token");
                    return;
                }
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
