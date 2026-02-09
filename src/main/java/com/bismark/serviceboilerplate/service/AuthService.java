package com.bismark.serviceboilerplate.service;

import com.bismark.serviceboilerplate.dto.RequestAuthLoginDto;
import com.bismark.serviceboilerplate.dto.JwtAccessTokenDto;
import com.bismark.serviceboilerplate.security.UserDetailServiceImpl;
import com.bismark.serviceboilerplate.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    UserDetailServiceImpl userDetailService;

    public JwtAccessTokenDto accountLogin(RequestAuthLoginDto requestAuthLoginDto) throws UsernameNotFoundException {
        UserDetails userDetails = userDetailService.loadUserByUsername(requestAuthLoginDto.getUsername());
        if (!userDetailService.passwordEncoder().matches(requestAuthLoginDto.getPassword(), userDetails.getPassword())) {
            throw new UsernameNotFoundException("Username Password is not valid");
        }

        return JwtUtil.generateAccessToken(userDetails, jwtSecret);
    }
}
