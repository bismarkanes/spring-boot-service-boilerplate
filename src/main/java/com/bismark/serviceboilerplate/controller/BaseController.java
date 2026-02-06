package com.bismark.serviceboilerplate.controller;

import com.bismark.serviceboilerplate.dto.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class BaseController {
    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<@NonNull ResponseDto<@NonNull Object>> root(HttpServletRequest request) {
        String uri = request.getRequestURI();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = "Unknown";
        if (auth != null) {
            username = auth.getName();
        }
        ResponseDto<Object> responseDto = ResponseDto.builder().data("Hello " + username + ". You are allowed to access this private page " + uri).build();
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/public")
    public String welcome(Model model) {
        model.addAttribute("name", "World");
        return "index";
    }
}
