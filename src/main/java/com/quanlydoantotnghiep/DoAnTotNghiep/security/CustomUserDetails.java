package com.quanlydoantotnghiep.DoAnTotNghiep.security;

import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private Account account;

    public CustomUserDetails(Account account) {
        this.account = account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        account.getRoles().forEach((item) -> {
            authorities.add(new SimpleGrantedAuthority(item.getRoleName()));
        });

        return authorities;
    }

    @Override
    public String getPassword() {

        return account.getPassword();
    }

    @Override
    public String getUsername() {

        return account.getEmail();
    }

    @Override
    public boolean isEnabled() {

        return account.isEnable();
    }
}
