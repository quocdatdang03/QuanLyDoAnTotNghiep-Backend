package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;

import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Account;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.RefreshToken;
import com.quanlydoantotnghiep.DoAnTotNghiep.exception.ApiException;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.AccountRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.RefreshTokenRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final AccountRepository accountRepository;

    @Override
    public RefreshToken createRefreshToken(String userName) {

        Account account = accountRepository.findByEmail(userName)
                            .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Account is not exists with given userName:"+userName));

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(1000*60*2)) // 2 minutes
                .account(account).build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpirationOfToken(RefreshToken refreshToken) {

        // if refreshToken  expired -> delete refreshToken in DB
        if(refreshToken.getExpiryDate().compareTo(Instant.now())<0)
        {
            refreshTokenRepository.delete(refreshToken);
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Refresh Token was expired, please make a new sign in request");
        }

        // else -> return refreshToken
        return refreshToken;
    }

    @Override
    public boolean checkExistedRefreshToken(Account account) {

        RefreshToken refreshToken = refreshTokenRepository.findByAccount_AccountId(account.getAccountId());

        if(refreshToken!=null)
            return true;

        return false;
    }
}
