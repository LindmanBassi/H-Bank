package br.com.bassi.h_bank.exception;

public abstract class HBankException extends RuntimeException{

    public HBankException(String message) {
        super(message);
    }

    public HBankException(Throwable cause) {
        super(cause);
    }
}
