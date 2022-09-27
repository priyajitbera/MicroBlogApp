package com.priyajit.microblogapp.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenGeneratorFilter extends OncePerRequestFilter {

    @Value("${jwt.key}")
    private String JWT_KEY;

    @Value("${jwt.issuer}")
    private String JWT_ISSUER;

    @Value("${jwt.header}")
    private String JWT_HEADER;

    @Value("${jwt.prefix}")
    private String JWT_PREFIX;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // if authenticated create JWT token and include in Headers
        if (auth != null) {
            SecretKey key = Keys.hmacShaKeyFor(JWT_KEY.getBytes(StandardCharsets.UTF_8));
            String jwt = Jwts.builder()
                    .setIssuer(JWT_ISSUER)
                    .setSubject("JWT Token")
                    .claim("username", auth.getName())
                    .claim("authorities", authoritesToString(auth.getAuthorities()))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(new Date().getTime() + 300000000))
                    .signWith(key)
                    .compact();
            response.setHeader(JWT_HEADER, JWT_PREFIX + " " + jwt); // "<JWT_PREFIX> <jwt>"
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/open");
    }

    private String authoritesToString(Collection<? extends GrantedAuthority> authorities) {
        Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority authority : authorities) {
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }

}
