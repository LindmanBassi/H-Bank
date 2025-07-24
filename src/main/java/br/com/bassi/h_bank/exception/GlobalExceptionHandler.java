package br.com.bassi.h_bank.exception;

import br.com.bassi.h_bank.exception.dto.InvalidParamDto;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HBankException.class)
    public ProblemDetail handWalletHBankException(HBankException e){

        return e.toProblemDetail();
    }

    @ExceptionHandler (MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
      var invalidParams =  e.getFieldErrors()
                .stream()
                .map(fe -> new InvalidParamDto(fe.getField(), fe.getDefaultMessage()))
                .toList();

        var pd =  ProblemDetail.forStatus(400);
        pd.setTitle("Invalid request parameters");
        pd.setDetail("There is invalid fields on the request");
        pd.setProperty("invalid-params", invalidParams);
        return pd;
    }

}
