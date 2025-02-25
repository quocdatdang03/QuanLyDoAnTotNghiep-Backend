package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.auth.request.LoginRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.auth.request.RefreshTokenRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.auth.request.ResetPasswordRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.auth.response.AuthResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {

        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {

        return ResponseEntity.ok(authService.refreshJwtToken(refreshTokenRequest));
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> sendResetPasswordEmail(@RequestParam("email") String email) {

        return ResponseEntity.ok(authService.sendEmailResetPassword(email));
    }

//    @PostMapping("/resetPassword")
//    public ResponseEntity<ResetPasswordResponse> verifyResetPasswordInfo(@RequestBody ResetPasswordInfoRequest request) {
//
//        return ResponseEntity.ok(authService.getUserResetPasswordInfo(request));
//    }

    @PutMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {

        return ResponseEntity.ok(authService.resetPassword(resetPasswordRequest));
    }

}

