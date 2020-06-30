package br.com.caelum.mamute.user.api;

import br.com.caelum.mamute.infrastructure.sanitized.text.SanitizedText;
import br.com.caelum.mamute.user.domain.LoginMethod;
import br.com.caelum.mamute.user.domain.UserEntity;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupResource {

    @NotBlank(message = "{signup.message.validation.name}")
    private String name;

    @NotBlank(message = "{signup.message.validation.email}")
    private String email;

    @NotBlank(message = "{signup.message.validation.password}")
    @Size(min = 6, max = 100, message = "{signup.errors.password.length}")
    private String password;

    @NotBlank(message = "{signup.message.validation.confirm.password}")
    private String confirmPassword;

    public UserEntity toEntity() {
        final UserEntity newUser = new UserEntity(SanitizedText.fromTrustedText(this.name), this.email);
        newUser.add(LoginMethod.brutalLogin(newUser, this.email, this.password));
        return newUser;
    }
}