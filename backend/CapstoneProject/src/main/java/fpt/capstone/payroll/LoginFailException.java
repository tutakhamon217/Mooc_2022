package fpt.capstone.payroll;

public class LoginFailException extends RuntimeException {
    public LoginFailException(String username) {
        super("Failed to login by user: " + username);
    }
}
