package br.com.bassi.h_bank.repositories;

import br.com.bassi.h_bank.domain.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DepositRepository extends JpaRepository<Deposit, UUID> {
}
