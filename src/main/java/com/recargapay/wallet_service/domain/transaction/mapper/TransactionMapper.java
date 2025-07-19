package com.recargapay.wallet_service.domain.transaction.mapper;

import com.recargapay.wallet_service.domain.transaction.enums.TransactionType;
import com.recargapay.wallet_service.infrastructure.persistence.transaction.entity.TransactionEntity;
import com.recargapay.wallet_service.infrastructure.persistence.wallet.entity.WalletEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    TransactionEntity toTransactionEntity(WalletEntity wallet, BigDecimal amount, TransactionType transactionType);

}
