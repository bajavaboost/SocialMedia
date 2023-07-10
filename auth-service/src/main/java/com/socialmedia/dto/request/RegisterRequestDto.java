package com.socialmedia.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegisterRequestDto {

    @NotEmpty(message = "Kullanıcı adını boş bırakmayınız.")
    @Size(min = 3, max = 20, message = "Kullanıcı adı en az 3 en fazla 20 karakter olabilir.")
    private String username;
    @Email(message = "Lütfen geçerli bir email giriniz.")
    private String email;

    //@NotBlank --> username = ""
    //@NotNull  --> username == null (swaggerdan veri silinmiş)
    @NotEmpty // Yukarıdaki her ikisini bir den kontrol eden anotasyondur.
    @Size(min = 8, max = 32, message = "Şifre en az 8 en çok 32 karakter olabilir.")
    private String password;
    private String rePassword;
}
