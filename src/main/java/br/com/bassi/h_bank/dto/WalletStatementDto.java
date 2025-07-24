package br.com.bassi.h_bank.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletStatementDto(UUID walletId,
                                 String cpf,
                                 String name,
                                 String email,
                                 BigDecimal balance) {
}
