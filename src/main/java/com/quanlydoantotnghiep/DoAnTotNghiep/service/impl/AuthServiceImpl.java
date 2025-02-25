package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.RoleDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.auth.request.LoginRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.auth.request.RefreshTokenRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.auth.request.ResetPasswordRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.auth.response.AuthResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Account;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.RefreshToken;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Role;
import com.quanlydoantotnghiep.DoAnTotNghiep.exception.ApiException;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.AccountRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.RefreshTokenRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.security.jwt.JwtTokenProvider;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.AuthService;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.EmailService;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.RefreshTokenService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailService emailService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    /* Authentication and refresh token */
    @Override
    public AuthResponse login(LoginRequest loginRequest) {

        Account accountFromDB = accountRepository.findByCode(loginRequest.getCode())
                                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Tài khoản hoặc mật khẩu không đúng"));

        // check if account is disable -> throw exception
        if(!accountFromDB.isEnable())
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Tài khoản của bạn đã bị khóa");

        Authentication authentication = null;
        try {
            // authenticate -> will throw exception 401 (e.g BadCredentialsException) if username or password is not correct
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getCode(),
                            loginRequest.getPassword()
                    )
            );
        }
        catch (BadCredentialsException e) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Tài khoản hoặc mật khẩu không đúng");
        }

        // Save authentication info in SecurityContextHolder  for serving next request
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtTokenProvider.generateToken(authentication);

        // get role:
        Set<Role> roles = null;
        roles = accountFromDB.getRoles();
        Set<RoleDto> roleDtos = roles.stream()
                                .map(role -> modelMapper.map(role, RoleDto.class))
                                .collect(Collectors.toSet());

        // check if refresh token of current user is already existed in DB -> delete refreshToken
        if(refreshTokenService.checkExistedRefreshToken(accountFromDB))
        {
            // delete refreshToken from DB
            refreshTokenRepository.deleteByAccount_AccountId(accountFromDB.getAccountId());
        }

        // create new refresh Token:
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(accountFromDB.getEmail());

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .message("User Logged-In Successfully!")
                .tokenType("Bearer")
                .roles(roleDtos).build();

        return authResponse;
    }

    @Override
    public AuthResponse refreshJwtToken(RefreshTokenRequest refreshTokenRequest) {

        return refreshTokenRepository.findByToken(refreshTokenRequest.getRefreshToken())
                .map(refreshToken -> refreshTokenService.verifyExpirationOfToken(refreshToken)) // verify expiration of token
                .map(refreshToken -> refreshToken.getAccount()) // get Account info by refresh Token
                .map(account -> {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            account.getCode(), account.getPassword()
                    );
                    String accessToken = jwtTokenProvider.generateToken(authentication);

                    AuthResponse authResponse = new AuthResponse();
                    authResponse.setAccessToken(accessToken);
                    authResponse.setRefreshToken(refreshTokenRequest.getRefreshToken());

                    return authResponse;
                }).orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "InvalidRefreshToken"));
    }

    /* For got password function */
    @Override
    public String sendEmailResetPassword(String email) {

        Account account = accountRepository.findByEmail(email)
                        .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tồn tại email "+email+" trong hệ thống, vui lòng nhập email hợp lệ."));

        String resetPasswordVerificationCode = generateResetPasswordVerificationCode();

        account.setResetPasswordVerificationCode(resetPasswordVerificationCode);
        account.setResetPasswordVerificationCodeExpiredAt(LocalDateTime.now().plusMinutes(2));

        Account savedAccount = accountRepository.save(account);

        String subject = "Reset Your Password";
        String verificationCode = savedAccount.getResetPasswordVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the reset password verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendEmail(email, subject, htmlMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return "Gửi email đặt lại mật khẩu thành công, vui lòng kiểm tra email để nhận mã xác thực";
    }

    @Override
    public String resetPassword(ResetPasswordRequest resetPasswordRequest) {

        Account account = accountRepository.findByEmail(resetPasswordRequest.getEmail())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Tài khoản có email "+resetPasswordRequest.getEmail()+" không tồn tại"));

        if(!account.getResetPasswordVerificationCode().equals(resetPasswordRequest.getResetPasswordVerificationCode()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Mã xác thực không đúng.");

        if(account.getResetPasswordVerificationCodeExpiredAt().isBefore(LocalDateTime.now()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Mã xác thực đã hết hạn, vui lòng gửi email mới để đặt lại mật khẩu.");

        if(passwordEncoder.matches(resetPasswordRequest.getNewPassword(), account.getPassword()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Mật khẩu mới không thể giống mật khẩu cũ.");

        account.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        account.setResetPasswordVerificationCode(null);
        account.setResetPasswordVerificationCodeExpiredAt(null);

        accountRepository.save(account);

        return "Mật khẩu đã được thay đổi thành công.";
    }

    private String generateResetPasswordVerificationCode() {
        Random random = new Random();

        int verificationCode = random.nextInt(900000)+100000;

        return String.valueOf(verificationCode);
    }

}
 