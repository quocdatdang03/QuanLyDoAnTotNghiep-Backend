package com.quanlydoantotnghiep.DoAnTotNghiep.dto.auth.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ResetPasswordResponse {

    String email;
    String resetPasswordVerificationCode;
}
