package fpt.capstone.payroll;

import java.util.List;

public class FileParserException extends RuntimeException {
    public List<String> errors;


    public FileParserException(List<String> errors) {
        this.errors = errors;
    }



    public List<String> getErrors() {
        return this.errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

}
