package com.quanlydoantotnghiep.DoAnTotNghiep.security;

import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Account;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class InstructorAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        Authentication auth = authentication.get();

        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (auth == null || !auth.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }

        // Kiểm tra xem người dùng có role GIANGVIEN không
        boolean isInstructor = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("GIANGVIEN"));

        if (!isInstructor) {

            return new AuthorizationDecision(false);
        }

        // Lấy CustomUserDetails và kiểm tra isLeader
        if (auth.getPrincipal() instanceof CustomUserDetails) {

            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            Account account = userDetails.getAccount();
            Teacher teacher = account.getTeacher();

            return new AuthorizationDecision(teacher != null && teacher.isLeader()); // return true nếu là GIANGVIEN và isLeader=true
        }


        return new AuthorizationDecision(false);
    }
}
