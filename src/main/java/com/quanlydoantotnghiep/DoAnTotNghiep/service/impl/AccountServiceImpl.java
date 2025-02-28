package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.StudentAccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.TeacherAccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.request.UpdateAccountClientRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.clazz.ClassDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Account;
import com.quanlydoantotnghiep.DoAnTotNghiep.exception.ApiException;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.AccountRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.security.jwt.JwtTokenProvider;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ModelMapper modelMapper;

    @Override
    public AccountDto getByJwtToken(String jwtToken) {

        String code = jwtTokenProvider.getUsername(jwtToken);

        Account account = accountRepository.findByCode(code)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Account is not exists with given code: "+code));

        return getAccountDto(account);
    }

    @Override
    public AccountDto findByEmail(String email) {

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Account is not exists with given email: "+email));

        return getAccountDto(account);
    }

    @Override
    public AccountDto updateAccountProfile(String userEmail, UpdateAccountClientRequest request) {

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Account is not exists with given id: "+request.getAccountId()));

        if(!account.getEmail().equals(userEmail))
            throw new ApiException(HttpStatus.FORBIDDEN, "You don't have permission to do this action");

        account.setAddress(request.getAddress());
        account.setPhoneNumber(request.getPhoneNumber());
        account.setImage(request.getImage());

        Account savedAccount = accountRepository.save(account);

        return getAccountDto(savedAccount);
    }

    private AccountDto getAccountDto(Account account) {
        AccountDto accountDto = modelMapper.map(account, AccountDto.class);

        if(account.getStudent()!=null) {

            StudentAccountDto studentAccountDto = modelMapper.map(account.getStudent(), StudentAccountDto.class);
            studentAccountDto.setStudentClass(modelMapper.map(account.getStudent().getClazz(), ClassDto.class));

            accountDto.setUserDetails(studentAccountDto);
        }
        else if(account.getTeacher()!=null) {

            TeacherAccountDto teacherAccountDto = modelMapper.map(account.getTeacher(), TeacherAccountDto.class);
            accountDto.setUserDetails(teacherAccountDto);
        }

        return accountDto;
    }

}
