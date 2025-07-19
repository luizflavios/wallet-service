package com.recargapay.wallet_service.domain.wallet.model;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateWalletRequestDTO(@NotNull UUID userId) {
}
