package com.recargapay.wallet_service.domain.transaction;

import com.recargapay.wallet_service.domain.transaction.mapper.TransactionMapper;
import com.recargapay.wallet_service.infrastructure.persistence.transaction.TransactionRepository;
import com.recargapay.wallet_service.infrastructure.persistence.wallet.entity.WalletEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.recargapay.wallet_service.domain.transaction.enums.TransactionType.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public BigDecimal retrieveBalanceAt(WalletEntity wallet, LocalDateTime at) {
        return transactionRepository.calculateBalanceAt(wallet, at);
    }

    public void registerDepositTransaction(WalletEntity wallet, BigDecimal amount) {
        var transactionEntity = TransactionMapper.INSTANCE.toTransactionEntity(wallet, amount, DEPOSIT);

        transactionRepository.save(transactionEntity);

        log.info("Deposit made - {} - {}", transactionEntity.getId(), wallet.getId());
    }

    public void registerWithdrawTransaction(WalletEntity wallet, BigDecimal amount) {
        var transactionEntity = TransactionMapper.INSTANCE.toTransactionEntity(wallet, amount, WITHDRAW);

        transactionRepository.save(transactionEntity);

        log.info("Withdrawal made - {} - {}", transactionEntity.getId(), wallet.getId());
    }

    public void registerTransferOut(WalletEntity wallet, BigDecimal amount) {
        var transactionEntity = TransactionMapper.INSTANCE.toTransactionEntity(wallet, amount, TRANSFER_OUT);

        transactionRepository.save(transactionEntity);

        log.info("Transfer out made - {} - {}", transactionEntity.getId(), wallet.getId());
    }

    public void registerTransferIn(WalletEntity wallet, BigDecimal amount) {
        var transactionEntity = TransactionMapper.INSTANCE.toTransactionEntity(wallet, amount, TRANSFER_IN);

        transactionRepository.save(transactionEntity);

        log.info("Transfer in made - {} - {}", transactionEntity.getId(), wallet.getId());
    }

}
