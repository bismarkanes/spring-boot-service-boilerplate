package com.bismark.serviceboilerplate.controller;

import com.bismark.serviceboilerplate.dto.RequestAuthLoginDto;
import com.bismark.serviceboilerplate.dto.ResponseAuthTokenDto;
import com.bismark.serviceboilerplate.dto.JwtAccessTokenDto;
import com.bismark.serviceboilerplate.service.AuthService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<@NonNull ResponseAuthTokenDto> Login(@RequestBody RequestAuthLoginDto requestAuthLoginDto) {
        JwtAccessTokenDto ac = authService.accountLogin(requestAuthLoginDto);
        ResponseAuthTokenDto response = new ResponseAuthTokenDto(ac.getToken(), ac.getExpiredDate());
        return ResponseEntity.ok(response);
    }
}
