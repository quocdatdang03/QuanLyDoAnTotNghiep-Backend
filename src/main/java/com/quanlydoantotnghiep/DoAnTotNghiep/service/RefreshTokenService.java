package com.quanlydoantotnghiep.DoAnTotNghiep.service;


import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Account;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.RefreshToken;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(String userName);
    RefreshToken verifyExpirationOfToken(RefreshToken refreshToken);
    boolean checkExistedRefreshToken(Account account);
}
