package com.quanlydoantotnghiep.DoAnTotNghiep.security;

import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Account;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Student;
import com.quanlydoantotnghiep.DoAnTotNghiep.exception.ApiException;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = accountRepository.findByCode(username)
                                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Account is not exists with given code: "+username));

        // check if account is student is not in current semester -> throw exception to prevent login
        if(account.getStudent()!=null) {

            Student student = account.getStudent();

            boolean isInCurrentSemester = student.getStudentSemesters().stream()
                    .anyMatch(item -> item.getSemester().isCurrent() && !item.isFlagDelete());

            if(!isInCurrentSemester)
                throw new ApiException(HttpStatus.UNAUTHORIZED, "Sinh viên chưa đăng ký đồ án tốt nghiệp trong học kỳ hiện tại,  không thể đăng nhập");
        }

        return new CustomUserDetails(account);
    }
}
