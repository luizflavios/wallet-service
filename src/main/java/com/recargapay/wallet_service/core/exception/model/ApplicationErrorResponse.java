package com.recargapay.wallet_service.core.exception.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record ApplicationErrorResponse(
        String error,
        String detail,
        HttpStatus status
) {
}
