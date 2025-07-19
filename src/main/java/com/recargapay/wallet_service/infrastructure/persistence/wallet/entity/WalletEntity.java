package com.recargapay.wallet_service.infrastructure.persistence.wallet.entity;

import com.recargapay.wallet_service.domain.wallet.exception.InsufficientBalanceException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "wallets")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class WalletEntity {

    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "user_id", nullable = false, updatable = false, unique = true)
    private UUID user;

    @Column(name = "current_balance", nullable = false)
    private BigDecimal currentBalance;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Version
    @Column(nullable = false)
    private Long version;


    public void creditAmountIntoBalance(BigDecimal amount) {
        setCurrentBalance(getCurrentBalance().add(amount));
    }

    public void debitAmountIntoBalance(BigDecimal amount) {
        checkSufficientBalance(amount);

        setCurrentBalance(getCurrentBalance().subtract(amount));
    }

    private void checkSufficientBalance(BigDecimal amount) {
        if (getCurrentBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {

            throw new InsufficientBalanceException(getId());

        }
    }

}
