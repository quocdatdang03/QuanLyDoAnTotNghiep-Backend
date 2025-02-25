package com.quanlydoantotnghiep.DoAnTotNghiep.dto.account;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDto {

    String roleId;
    String roleName;
}
