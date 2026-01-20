package com.bismark.serviceboilerplate.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseDto<T> {
    private T data;
}
