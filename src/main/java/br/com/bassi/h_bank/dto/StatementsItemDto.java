package br.com.bassi.h_bank.dto;

import br.com.bassi.h_bank.dto.enun.StatementOperation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record StatementsItemDto(String statementId,
                                String type,
                                String literal,
                                BigDecimal value,
                                LocalDateTime dateTime,
                                StatementOperation operation) {
}
