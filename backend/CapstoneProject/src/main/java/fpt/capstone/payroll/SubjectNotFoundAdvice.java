package fpt.capstone.payroll;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import fpt.capstone.entities.ServiceResult;

@ControllerAdvice
public class SubjectNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(SubjectNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServiceResult<String> subjectNotFoundHandler(SubjectNotFoundException ex)
    {
        return new ServiceResult<String>(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getMessage());
    }
}
