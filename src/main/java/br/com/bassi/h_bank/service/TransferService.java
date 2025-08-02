package br.com.bassi.h_bank.service;

import br.com.bassi.h_bank.domain.Transfer;
import br.com.bassi.h_bank.domain.Wallet;
import br.com.bassi.h_bank.dto.TransferMoneyDto;
import br.com.bassi.h_bank.exception.TransferException;
import br.com.bassi.h_bank.exception.WalletNotFoundException;
import br.com.bassi.h_bank.repositories.TransferRepository;
import br.com.bassi.h_bank.repositories.WalletRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final WalletRepository walletRepository;

    public TransferService(TransferRepository transferRepository, WalletRepository walletRepository) {
        this.transferRepository = transferRepository;
        this.walletRepository = walletRepository;
    }
    @Transactional
    public void transferMoney(TransferMoneyDto dto) {

        var sender = walletRepository.findById(dto.sender())
                .orElseThrow(()-> new WalletNotFoundException("Sender does not exits"));

        var receiver = walletRepository.findById(dto.receiver())
                .orElseThrow(()-> new WalletNotFoundException("Receiver does not exits"));

        if(sender.getBalance().compareTo(dto.value())== -1){
            throw new TransferException(
                    "Insufficient balance. Your current balance is $" + sender.getBalance());
        }

        updateWallets(dto, sender, receiver);
        persistTransfer(dto, receiver, sender);

    }

    private void updateWallets(TransferMoneyDto dto, Wallet sender, Wallet receiver) {

        sender.setBalance(sender.getBalance().subtract(dto.value()));
        receiver.setBalance(receiver.getBalance().add(dto.value()));
        walletRepository.save(sender);
        walletRepository.save(receiver);

    }

    private void persistTransfer(TransferMoneyDto dto, Wallet receiver, Wallet sender) {

        var transfer = new Transfer();
        transfer.setReceiver(receiver);
        transfer.setSender(sender);
        transfer.setTransferValue(dto.value());
        transfer.setTransferDateTime(LocalDateTime.now());
        transferRepository.save(transfer);

    }
}
