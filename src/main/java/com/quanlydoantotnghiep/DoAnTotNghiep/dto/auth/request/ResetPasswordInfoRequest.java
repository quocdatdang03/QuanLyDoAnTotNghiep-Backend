package com.quanlydoantotnghiep.DoAnTotNghiep.dto.auth.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResetPasswordInfoRequest {

    String email;
    String resetPasswordTicket;
}
