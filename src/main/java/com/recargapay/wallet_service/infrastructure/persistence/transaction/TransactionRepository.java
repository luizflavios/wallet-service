package com.recargapay.wallet_service.infrastructure.persistence.transaction;

import com.recargapay.wallet_service.infrastructure.persistence.transaction.entity.TransactionEntity;
import com.recargapay.wallet_service.infrastructure.persistence.wallet.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {


    @Query("""
             SELECT COALESCE(SUM(CASE WHEN t.transactionType IN (0, 2) THEN t.amount ELSE -t.amount END), 0)
             FROM TransactionEntity t
             WHERE t.wallet = :wallet AND t.createdAt <= :at
            """)
    BigDecimal calculateBalanceAt(WalletEntity wallet, LocalDateTime at);

}
