package br.com.bassi.h_bank.repositories;

import br.com.bassi.h_bank.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    Optional<Wallet> findByCpfOrEmail(String cpf, String email);
}
