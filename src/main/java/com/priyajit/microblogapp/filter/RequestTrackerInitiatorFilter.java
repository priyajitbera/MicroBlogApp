package com.priyajit.microblogapp.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.priyajit.microblogapp.entity.RequestTracker;

@Component
public class RequestTrackerInitiatorFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // generate an unique requestId and set in header and as attribute
        String requestId = RequestTracker.generatedRequestId();
        request.setAttribute("requestId", requestId);
        response.setHeader("requestId", requestId);

        filterChain.doFilter(request, response);
    }

}
