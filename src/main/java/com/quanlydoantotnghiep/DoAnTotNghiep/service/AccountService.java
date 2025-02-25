package com.quanlydoantotnghiep.DoAnTotNghiep.service;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.request.UpdateAccountClientRequest;

public interface AccountService {

    AccountDto getByJwtToken(String jwtToken);
    AccountDto findByEmail(String email);
    AccountDto updateAccountProfile(String userEmail, UpdateAccountClientRequest request);
}
