package com.recargapay.wallet_service.domain.wallet.mapper;

import com.recargapay.wallet_service.domain.wallet.model.CreateWalletResponseDTO;
import com.recargapay.wallet_service.infrastructure.persistence.wallet.entity.WalletEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.UUID;

@Mapper(imports = {BigDecimal.class})
public interface WalletMapper {

    WalletMapper INSTANCE = Mappers.getMapper(WalletMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "currentBalance", expression = "java(BigDecimal.ZERO)")
    WalletEntity toEntity(UUID user);

    CreateWalletResponseDTO toDTO(WalletEntity wallet);

}
