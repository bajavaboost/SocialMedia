package com.socialmedia.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    USER_NOT_FOUND(5100,"Böyle bir Kullanıcı Bulunamadı",HttpStatus.NOT_FOUND),
    ACCOUNT_NOT_ACTIVE(4100,"Hesabınız Aktif değil",HttpStatus.BAD_REQUEST),
    INVALID_CODE(4101,"Geçersiz Kod",HttpStatus.BAD_REQUEST),
    INTERNAL_ERROR(6000,"Sunucu Hatası",HttpStatus.INTERNAL_SERVER_ERROR),
    ALREADY_ACTIVE(4200,"Hesabınız Zaten Aktif",HttpStatus.BAD_REQUEST);

    private int code;
    private String message;
    HttpStatus httpStatus;
}
