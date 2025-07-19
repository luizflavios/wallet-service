package com.recargapay.wallet_service.domain.wallet.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateWalletResponseDTO(UUID id, LocalDateTime createdAt) {
}
