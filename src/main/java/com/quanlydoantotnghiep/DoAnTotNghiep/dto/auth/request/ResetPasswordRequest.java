package com.quanlydoantotnghiep.DoAnTotNghiep.dto.auth.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResetPasswordRequest {

    String email;
    String resetPasswordVerificationCode;
    String newPassword;
}
