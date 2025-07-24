package br.com.bassi.h_bank.dto;

import java.util.List;

public record StatementDto(WalletStatementDto wallet,
                           List<StatementsItemDto> statements,
                           PaginationDto pagination) {
}
