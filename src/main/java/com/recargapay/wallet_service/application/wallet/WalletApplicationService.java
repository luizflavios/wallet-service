package com.recargapay.wallet_service.application.wallet;

import com.recargapay.wallet_service.domain.transaction.TransactionService;
import com.recargapay.wallet_service.domain.transaction.model.TransactionAmountRequestDTO;
import com.recargapay.wallet_service.domain.transaction.model.TransferRequestDTO;
import com.recargapay.wallet_service.domain.wallet.WalletService;
import com.recargapay.wallet_service.domain.wallet.exception.IllegalTransferException;
import com.recargapay.wallet_service.domain.wallet.mapper.WalletMapper;
import com.recargapay.wallet_service.domain.wallet.model.BalanceResponseDTO;
import com.recargapay.wallet_service.domain.wallet.model.CreateWalletRequestDTO;
import com.recargapay.wallet_service.domain.wallet.model.CreateWalletResponseDTO;
import com.recargapay.wallet_service.infrastructure.persistence.wallet.entity.WalletEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalletApplicationService {

    private final WalletService walletService;
    private final TransactionService transactionService;

    public CreateWalletResponseDTO createWallet(CreateWalletRequestDTO createWalletRequest) {
        var wallet = walletService.createWallet(createWalletRequest.userId());

        log.info("Wallet created - {}", wallet.getId());

        return WalletMapper.INSTANCE.toDTO(wallet);
    }

    public BalanceResponseDTO retrieveCurrentBalance(UUID walletId) {
        var currentBalance = walletService.retrieveCurrentBalance(walletId);

        log.info("Retrieved current balance - {}", walletId);

        return new BalanceResponseDTO(currentBalance);
    }

    public BalanceResponseDTO retrieveBalanceAt(UUID walletId, LocalDateTime at) {
        var wallet = walletService.findWalletById(walletId);

        var balance = transactionService.retrieveBalanceAt(wallet, at);

        log.info("Retrieved balance at {} - {}", at, walletId);

        return new BalanceResponseDTO(balance);
    }

    @Transactional
    public void deposit(UUID walletId, TransactionAmountRequestDTO transactionAmountRequestDTO) {
        var wallet = walletService
                .creditAmountIntoBalanceAndReturnWallet(walletId, transactionAmountRequestDTO.amount());

        transactionService.registerDepositTransaction(wallet, transactionAmountRequestDTO.amount());
    }

    @Transactional
    public void withdraw(UUID walletId, TransactionAmountRequestDTO transactionAmountRequestDTO) {
        var wallet = walletService
                .debitAmountIntoBalanceAndReturnWallet(walletId, transactionAmountRequestDTO.amount());

        transactionService.registerWithdrawTransaction(wallet, transactionAmountRequestDTO.amount());
    }

    @Transactional
    public void transfer(TransferRequestDTO transferRequestDTO) {
        checkLegalityOfTheTransfer(transferRequestDTO);

        WalletEntity sender;
        WalletEntity recipient;

        var transferAmount = transferRequestDTO.amount();

        if (transferRequestDTO.senderId()
                .compareTo(transferRequestDTO.recipientId()) < 0) {
            sender = walletService.debitAmountIntoBalanceAndReturnWallet(transferRequestDTO.senderId(), transferAmount);

            recipient = walletService.creditAmountIntoBalanceAndReturnWallet(transferRequestDTO.recipientId(), transferAmount);
        } else {
            recipient = walletService.creditAmountIntoBalanceAndReturnWallet(transferRequestDTO.recipientId(), transferAmount);

            sender = walletService.debitAmountIntoBalanceAndReturnWallet(transferRequestDTO.senderId(), transferAmount);
        }

        transactionService.registerTransferOut(sender, transferAmount);

        transactionService.registerTransferIn(recipient, transferAmount);
    }

    private void checkLegalityOfTheTransfer(TransferRequestDTO transferRequestDTO) {
        if (transferRequestDTO.senderId()
                .equals(transferRequestDTO.recipientId())) {

            throw new IllegalTransferException();
        }
    }
}
