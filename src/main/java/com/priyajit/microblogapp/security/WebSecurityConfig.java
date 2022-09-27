package com.priyajit.microblogapp.security;

import java.util.Arrays;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.priyajit.microblogapp.filter.JwtTokenGeneratorFilter;
import com.priyajit.microblogapp.filter.JwtTokenValidatorFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    final static String[] UNAUTHENTICATED_ENPOINTS = { "/open" };

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtTokenGeneratorFilter jwtTokenGeneratorFilter;

    @Autowired
    private JwtTokenValidatorFilter jwtTokenValidatorFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                // ~ cors configuration
                .cors().configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOriginPatterns(Collections.singletonList("*"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setExposedHeaders(Arrays.asList("Authorization"));
                        config.setMaxAge(3600L);
                        return config;
                    }

                }).and()
                .csrf().disable()

                // ~ filters
                .addFilterBefore(jwtTokenValidatorFilter,
                        BasicAuthenticationFilter.class)
                .addFilterAfter(jwtTokenGeneratorFilter,
                        BasicAuthenticationFilter.class)

                // ~ authentication type
                .httpBasic().and()

                // ~ public URLs
                .authorizeRequests().antMatchers(UNAUTHENTICATED_ENPOINTS).permitAll()

                .anyRequest().authenticated();

        http.authenticationProvider(authenticationProvider);
        return http.build();
    }
}
