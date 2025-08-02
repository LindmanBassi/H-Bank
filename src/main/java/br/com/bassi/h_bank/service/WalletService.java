package br.com.bassi.h_bank.service;

import br.com.bassi.h_bank.domain.Deposit;
import br.com.bassi.h_bank.domain.Wallet;
import br.com.bassi.h_bank.dto.*;
import br.com.bassi.h_bank.dto.enun.StatementOperation;
import br.com.bassi.h_bank.exception.DeleteWalletException;
import br.com.bassi.h_bank.exception.StatementException;
import br.com.bassi.h_bank.exception.WalletDataAlreadyExistsException;
import br.com.bassi.h_bank.exception.WalletNotFoundException;
import br.com.bassi.h_bank.repositories.DepositRepository;
import br.com.bassi.h_bank.repositories.WalletRepository;
import br.com.bassi.h_bank.repositories.dto.StatementView;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public StatementDto getStatements(UUID walletId, Integer page, Integer pageSize) {

       var wallet = walletRepository.findById(walletId)
                .orElseThrow(()-> new WalletNotFoundException("There is no wallet with this id"));

       var pageRequest = PageRequest.of(page,pageSize, Sort.Direction.DESC,"statement_date_time");

       var statements = walletRepository.findStatements(walletId.toString(),pageRequest)
               .map(view -> mapToDto(walletId,view));

       return new StatementDto(
         new WalletStatementDto(wallet.getWalletId(),wallet.getCpf(),wallet.getName(),wallet.getEmail(),wallet.getBalance())
               ,statements.getContent(),
               new PaginationDto(statements.getNumber(),statements.getSize(),statements.getTotalElements(),statements.getTotalPages())
       );
    }

    private StatementsItemDto mapToDto(UUID walletId, StatementView view) {


        if(view.getType().equalsIgnoreCase("deposit")){
            return mapToDeposit(view);
        }

        if(view.getType().equalsIgnoreCase("transfer")
        && view.getWalletSender().equalsIgnoreCase(walletId.toString())){

            return mapWhenTransferSent(walletId,view);
        }

        if(view.getType().equalsIgnoreCase("transfer")
                && view.getWalletReceiver().equalsIgnoreCase(walletId.toString())){

            return mapWhenTransferReceiver(walletId,view);
        }

        throw new StatementException("Invalid type " + view.getType());
    }

    private StatementsItemDto mapWhenTransferSent(UUID walletId, StatementView view) {

        return new StatementsItemDto(
                view.getStatementId(),
                view.getType(),
                "money sent to " + view.getWalletReceiver(),
                view.getStatementValue(),
                view.getStatementDateTime(),
                StatementOperation.DEBIT
        );
    }


    private StatementsItemDto mapWhenTransferReceiver(UUID walletId, StatementView view) {

        return new StatementsItemDto(
                view.getStatementId(),
                view.getType(),
                "money received from " + view.getWalletSender(),
                view.getStatementValue(),
                view.getStatementDateTime(),
                StatementOperation.CREDIT
        );
    }

    private StatementsItemDto mapToDeposit(StatementView view) {
        return new StatementsItemDto(
                view.getStatementId(),
                view.getType(),
                "money deposit",
                view.getStatementValue(),
                view.getStatementDateTime(),
                StatementOperation.CREDIT
        );
    }
}
