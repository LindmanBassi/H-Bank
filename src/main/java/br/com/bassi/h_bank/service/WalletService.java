package br.com.bassi.h_bank.service;

import br.com.bassi.h_bank.domain.Deposit;
import br.com.bassi.h_bank.domain.Wallet;
import br.com.bassi.h_bank.dto.DepositMoneyDto;
import br.com.bassi.h_bank.dto.WalletDto;
import br.com.bassi.h_bank.exception.DeleteWalletException;
import br.com.bassi.h_bank.exception.WalletDataAlreadyExistsException;
import br.com.bassi.h_bank.exception.WalletNotFoundException;
import br.com.bassi.h_bank.repositories.DepositRepository;
import br.com.bassi.h_bank.repositories.WalletRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final DepositRepository depositRepository;


    public WalletService(WalletRepository walletRepository, DepositRepository depositRepository) {
        this.walletRepository = walletRepository;
        this.depositRepository = depositRepository;
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

    public boolean deleteWallet(UUID walletid) {

        var wallet = walletRepository.findById(walletid);

        if (wallet.isPresent()) {

            if (wallet.get().getBalance().compareTo(BigDecimal.ZERO) != 0) {
                throw  new DeleteWalletException(
                        "the balance is not zero. The current amount is $" + wallet.get().getBalance());
            }

            walletRepository.deleteById(walletid);
        }

        return wallet.isPresent();
    }
    @Transactional
    public void depositMoney(UUID walletId,
                             DepositMoneyDto dto,
                             String ipAddress) {

       var wallet = walletRepository.findById(walletId)
                .orElseThrow(()-> new WalletNotFoundException("There is no wallet with this id"));

        var deposit = new Deposit();

        deposit.setWallet(wallet);
        deposit.setDepositValue(dto.value());
        deposit.setDepositDateTime(LocalDateTime.now());
        deposit.setIpAddress(ipAddress);

        depositRepository.save(deposit);

        wallet.setBalance(wallet.getBalance().add(dto.value()));

        walletRepository.save(wallet);
    }
}
