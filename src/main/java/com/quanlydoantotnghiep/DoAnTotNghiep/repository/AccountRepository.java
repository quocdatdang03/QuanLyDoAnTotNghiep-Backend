package com.quanlydoantotnghiep.DoAnTotNghiep.repository;

import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByCode(String code);
    Optional<Account> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByCode(String code);
    boolean existsByPhoneNumber(String phoneNumber);
}
