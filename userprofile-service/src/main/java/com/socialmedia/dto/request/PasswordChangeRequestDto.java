package com.socialmedia.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordChangeRequestDto {
    private String id;
    private String oldPassword;
    private String newPassword;
}
