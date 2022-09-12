package fpt.capstone.form_data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SignUpForm {
    @NotBlank
    @Size(min = 3, max = 20)
    private String login;
    private Set<String> role;
    @NotBlank
    @Size(min = 6, max = 20)
    private String password;


}
