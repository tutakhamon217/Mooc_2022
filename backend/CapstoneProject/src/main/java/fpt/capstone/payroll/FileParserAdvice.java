package fpt.capstone.payroll;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import fpt.capstone.entities.ServiceResult;

@ControllerAdvice
public class FileParserAdvice {
    @ResponseBody
    @ExceptionHandler(FileParserException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public ServiceResult<List<String>> handler(FileParserException ex)
    {
        return new ServiceResult<List<String>>(HttpStatus.PRECONDITION_FAILED, "failed to parse file",ex.getErrors());
    }
}
