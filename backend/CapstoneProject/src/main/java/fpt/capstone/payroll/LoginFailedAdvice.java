package fpt.capstone.payroll;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class LoginFailedAdvice {
    @ResponseBody
    @ExceptionHandler(LoginFailException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String loginFailedHandler(LoginFailException ex) {
        return ex.getMessage();
    }
}
