package com.recargapay.wallet_service.domain.wallet.exception;

import java.util.UUID;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(UUID walletId) {
        super(
                String.format("Insufficient balance - %s", walletId)
        );
    }

}
