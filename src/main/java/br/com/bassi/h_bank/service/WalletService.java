package br.com.bassi.h_bank.service;

import br.com.bassi.h_bank.domain.Wallet;
import br.com.bassi.h_bank.dto.WalletDto;
import br.com.bassi.h_bank.exception.WalletDataAlreadyExistsException;
import br.com.bassi.h_bank.repositories.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet createWallet(WalletDto dto) {

        var walletDb = walletRepository.findByCpfOrEmail(dto.cpf(),dto.email());
        if(walletDb.isPresent()){
            throw new WalletDataAlreadyExistsException("cpf or email already exists");
        }
        var wallet = new Wallet();
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setName(dto.name());
        wallet.setCpf(dto.cpf());
        wallet.setEmail(dto.email());

        return walletRepository.save(wallet);
    }
}
