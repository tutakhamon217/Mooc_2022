package fpt.capstone.form_data;

import javax.validation.constraints.NotBlank;

public class LoginForm {
    @NotBlank
    private String login;
    @NotBlank
    private String password;

    public LoginForm(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
