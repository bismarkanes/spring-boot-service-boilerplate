package com.bismark.serviceboilerplate.controller;

import com.bismark.serviceboilerplate.dto.UserDetailDto;
import com.bismark.serviceboilerplate.service.AccountService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    AccountService accountService;

    @GetMapping("/detail")
    public ResponseEntity<@NonNull UserDetailDto> getAccountDetail(Authentication authentication) {
        UserDetailDto userDetailDto = accountService.getAccountDetail(authentication.getName());
        return ResponseEntity.ok(userDetailDto);
    }

    @PostMapping
    public ResponseEntity<@NonNull UserDetailDto> createAccount(@RequestBody UserDetailDto userDetailDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(userDetailDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<@NonNull UserDetailDto> updateAccount(@PathVariable long id, @RequestBody UserDetailDto userDetailDto) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.updateAccount(id, userDetailDto));
    }
}
