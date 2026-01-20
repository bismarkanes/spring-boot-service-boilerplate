package com.bismark.serviceboilerplate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ResponseAuthTokenDto {
    private String token;
    private Date expiredDate;
}
