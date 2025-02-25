package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.request.UpdateAccountClientRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/profile")
    public ResponseEntity<AccountDto> showAccountProfile(@RequestHeader("Authorization") String jwtToken) {

        return ResponseEntity.ok(getAccountDtoByJwtToken(jwtToken));
    }

    @PutMapping("/profile")
    public ResponseEntity<AccountDto> updateAccountProfile(
            @RequestHeader("Authorization") String jwtToken,
            @RequestBody UpdateAccountClientRequest request
    ) {
        AccountDto accountDto = getAccountDtoByJwtToken(jwtToken);

        return ResponseEntity
                .ok(accountService.updateAccountProfile(accountDto.getEmail(), request));
    }

    private AccountDto getAccountDtoByJwtToken(String jwtToken) {

        String onlyToken = jwtToken.substring(7);

        return accountService.getByJwtToken(onlyToken);
    }
}
