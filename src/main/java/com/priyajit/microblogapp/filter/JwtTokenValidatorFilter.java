package com.priyajit.microblogapp.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenValidatorFilter extends OncePerRequestFilter {

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

        // if value has prefix "Bearer " then it's jwt token, format "Bearer <jwt>"
        String value = request.getHeader(JWT_HEADER); // JWT_HEADER : "Authorization"
        boolean isJwtToken = value != null && value.startsWith(JWT_PREFIX); // JWT_PREFIX : "Bearer "

        if (isJwtToken) {
            // get the jwt token excluding the JWT_PREFIX "Bearer" and white space " "
            String jwt = null;
            if (value != null)
                value.substring(JWT_PREFIX.length() + 1);
            try {
                SecretKey key = Keys.hmacShaKeyFor(JWT_KEY.getBytes(StandardCharsets.UTF_8));
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();
                String username = String.valueOf(claims.get("username"));
                String authorites = (String) claims.get("authorites");
                Authentication auth = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorites));

                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                throw new BadCredentialsException("Invalid JWT token received!");
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/open");
    }
}
