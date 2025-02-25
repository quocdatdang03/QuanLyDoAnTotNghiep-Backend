package com.quanlydoantotnghiep.DoAnTotNghiep.dto.auth.response;


import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.RoleDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AuthResponse {

    String accessToken;
    String refreshToken;
    String message;
    String tokenType = "Bearer";
    Set<RoleDto> roles;
}
