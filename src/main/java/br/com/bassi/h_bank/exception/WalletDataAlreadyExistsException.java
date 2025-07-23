package br.com.bassi.h_bank.exception;

public class WalletDataAlreadyExistsException extends HBankException {

    public WalletDataAlreadyExistsException(String message) {
        super(message);
    }
}
