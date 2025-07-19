package com.recargapay.wallet_service.domain.wallet.model;

import java.math.BigDecimal;

public record BalanceResponseDTO(
        BigDecimal currentBalance
) {
}
