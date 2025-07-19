package com.recargapay.wallet_service.entrypoint.controller;

import com.recargapay.wallet_service.application.wallet.WalletApplicationService;
import com.recargapay.wallet_service.domain.transaction.model.TransactionAmountRequestDTO;
import com.recargapay.wallet_service.domain.transaction.model.TransferRequestDTO;
import com.recargapay.wallet_service.domain.wallet.model.BalanceResponseDTO;
import com.recargapay.wallet_service.domain.wallet.model.CreateWalletRequestDTO;
import com.recargapay.wallet_service.domain.wallet.model.CreateWalletResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
@Tag(name = "Wallets")
public class WalletController {

    private final WalletApplicationService walletApplicationService;

    @PostMapping
    public ResponseEntity<CreateWalletResponseDTO> createWallet(@RequestBody @Valid
                                                                CreateWalletRequestDTO createWalletRequestDTO) {
        var walletResponse = walletApplicationService.createWallet(createWalletRequestDTO);

        return new ResponseEntity<>(walletResponse, CREATED);
    }

    @GetMapping("/{walletId}/balance")
    public ResponseEntity<BalanceResponseDTO> retrieveCurrentBalance(@PathVariable UUID walletId) {
        var currentBalanceResponse = walletApplicationService.retrieveCurrentBalance(walletId);

        return ResponseEntity.ok(currentBalanceResponse);
    }

    @GetMapping("/{walletId}/balance/history")
    public ResponseEntity<BalanceResponseDTO> retrieveBalanceAt(@PathVariable UUID walletId,
                                                                @RequestParam LocalDateTime at) {
        var balance = walletApplicationService.retrieveBalanceAt(walletId, at);

        return ResponseEntity.ok(balance);
    }

    @PostMapping("/{walletId}/deposit")
    public ResponseEntity<Void> deposit(@PathVariable UUID walletId, @RequestBody @Valid TransactionAmountRequestDTO
            transactionAmountRequestDTO) {
        walletApplicationService.deposit(walletId, transactionAmountRequestDTO);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{walletId}/withdraw")
    public ResponseEntity<Void> withdraw(@PathVariable UUID walletId, @RequestBody @Valid TransactionAmountRequestDTO
            transactionAmountRequestDTO) {
        walletApplicationService.withdraw(walletId, transactionAmountRequestDTO);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody @Valid TransferRequestDTO transferRequestDTO) {
        walletApplicationService.transfer(transferRequestDTO);

        return ResponseEntity.noContent().build();
    }


}
