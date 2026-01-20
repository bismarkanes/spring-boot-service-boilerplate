package com.bismark.serviceboilerplate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestAuthLoginDto {
    @Override
    public String toString() {
        return "username='" + username + '\'' +
                ", password='" + password + '\'';
    }

    private String username;
    private String password;
}
