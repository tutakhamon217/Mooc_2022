package fpt.capstone.payroll;

public class SubjectNotFoundException extends RuntimeException{
    public SubjectNotFoundException(String code) {
        super(code + " không tồn tại");
    }    
}
