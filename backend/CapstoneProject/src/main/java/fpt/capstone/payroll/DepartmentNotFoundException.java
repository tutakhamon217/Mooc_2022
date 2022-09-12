package fpt.capstone.payroll;

public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(String code) {
        super(code + "\'s department is not found");
    }
}
