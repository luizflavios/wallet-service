package com.recargapay.wallet_service.domain.wallet.exception;

import java.util.UUID;

public class AlreadyExistsWalletException extends RuntimeException {

    public AlreadyExistsWalletException(UUID userId) {
        super(
                String.format("Already exists wallet for user %s", userId)
        );
    }

}
