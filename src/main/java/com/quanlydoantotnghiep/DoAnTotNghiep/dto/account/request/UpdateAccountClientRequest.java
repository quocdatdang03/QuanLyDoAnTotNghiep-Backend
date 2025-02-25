package com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateAccountClientRequest {

    Long accountId;
    String phoneNumber;
    String address;
    String image;
}
