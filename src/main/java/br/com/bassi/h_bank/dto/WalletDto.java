package br.com.bassi.h_bank.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record WalletDto(@CPF @NotBlank String cpf,
                        @Email @NotBlank String email,
                        @NotBlank String name) {
}
