package com.quanlydoantotnghiep.DoAnTotNghiep.service;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.auth.request.LoginRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.auth.request.RefreshTokenRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.auth.request.ResetPasswordRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.auth.response.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginRequest loginRequest);
    AuthResponse refreshJwtToken(RefreshTokenRequest refreshTokenRequest);
    String sendEmailResetPassword(String email);
    String resetPassword(ResetPasswordRequest resetPasswordRequest);
//    ResetPasswordResponse getUserResetPasswordInfo(ResetPasswordInfoRequest request);
}
