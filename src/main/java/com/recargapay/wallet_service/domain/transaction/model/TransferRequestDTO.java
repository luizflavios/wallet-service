package com.recargapay.wallet_service.domain.transaction.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

import static com.recargapay.wallet_service.domain.transaction.constants.TransactionConstants.DEPOSIT_MIN_VALUE;

public record TransferRequestDTO(
        @NotNull UUID senderId,
        @NotNull UUID recipientId,
        @NotNull @DecimalMin(value = DEPOSIT_MIN_VALUE) BigDecimal amount
) {
}
