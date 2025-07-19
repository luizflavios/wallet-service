package com.recargapay.wallet_service.domain.wallet.exception;

public class IllegalTransferException extends RuntimeException {

    public IllegalTransferException() {
        super(
                "Sender and recipient wallets must be different"
        );
    }

}
