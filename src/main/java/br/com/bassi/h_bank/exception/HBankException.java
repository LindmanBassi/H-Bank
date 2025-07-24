package br.com.bassi.h_bank.exception;

import org.springframework.http.ProblemDetail;

public abstract class HBankException extends RuntimeException{

    public HBankException(String message) {
        super(message);
    }

    public HBankException(Throwable cause) {
        super(cause);
    }

    public ProblemDetail toProblemDetail(){

        var pd = ProblemDetail.forStatus(500);

        pd.setTitle("HBank Internal Server Error:");
        pd.setDetail("Contact HBank support");

        return pd;
    }
}
