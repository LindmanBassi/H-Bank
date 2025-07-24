package br.com.bassi.h_bank.repositories;

import br.com.bassi.h_bank.domain.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {
}
