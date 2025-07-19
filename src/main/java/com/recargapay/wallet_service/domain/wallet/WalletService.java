package com.recargapay.wallet_service.domain.wallet;

import com.recargapay.wallet_service.domain.wallet.exception.AlreadyExistsWalletException;
import com.recargapay.wallet_service.domain.wallet.mapper.WalletMapper;
import com.recargapay.wallet_service.infrastructure.persistence.wallet.WalletRepository;
import com.recargapay.wallet_service.infrastructure.persistence.wallet.entity.WalletEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    @Transactional
    public WalletEntity createWallet(UUID userId) {
        var optionalWallet = walletRepository.findByUser(userId);

        if (optionalWallet.isPresent()) {
            throw new AlreadyExistsWalletException(userId);
        }

        var wallet = WalletMapper.INSTANCE.toEntity(userId);

        return walletRepository.save(wallet);
    }

    @Transactional(readOnly = true)
    public BigDecimal retrieveCurrentBalance(UUID walletId) {
        var wallet = this.findWalletById(walletId);

        return wallet.getCurrentBalance();
    }

    public WalletEntity findWalletById(UUID walletId) {
        return walletRepository
                .findById(walletId)
                .orElseThrow(EntityNotFoundException::new);
    }

    public WalletEntity findWalletByIdForUpdate(UUID walletId) {
        return walletRepository
                .findByIdForUpdate(walletId)
                .orElseThrow(EntityNotFoundException::new);
    }

    public WalletEntity creditAmountIntoBalanceAndReturnWallet(UUID walletId, BigDecimal amount) {
        var wallet = findWalletByIdForUpdate(walletId);

        wallet.creditAmountIntoBalance(amount);

        walletRepository.save(wallet);

        log.info("Current balance increased - {}", walletId);

        return wallet;
    }

    public WalletEntity debitAmountIntoBalanceAndReturnWallet(UUID walletId, BigDecimal amount) {
        var wallet = findWalletByIdForUpdate(walletId);

        wallet.debitAmountIntoBalance(amount);

        walletRepository.save(wallet);

        log.info("Current balance decreased - {}", walletId);

        return wallet;
    }

}
