package com.bismark.serviceboilerplate.security;

import com.bismark.serviceboilerplate.utils.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    private UserDetailServiceImpl userDetailservice;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        List<String> headers = Collections.list(request.getHeaderNames());
        if (headers.contains("authorization")) {
            String authValue = request.getHeader("authorization");

            List<String> sList = Arrays.asList(authValue.split(" "));
            if (sList.size() < 2) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return;
            }
            if (!sList.get(0).equals("Bearer") || sList.get(1) == null || sList.get(1).isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return;
            }

            String token = sList.get(1);
            String username = "";

            try {
                username = JwtUtil.verifyToken(token, jwtSecret);
            } catch (JwtException exception) {
                logger.error("JwtFilter Error", exception);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailservice.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
