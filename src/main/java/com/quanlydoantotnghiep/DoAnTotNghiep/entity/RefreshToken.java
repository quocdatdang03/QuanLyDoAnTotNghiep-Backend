package com.quanlydoantotnghiep.DoAnTotNghiep.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="RefreshTokens")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long refreshTokenId;

    String token;

    Instant expiryDate;

    @OneToOne
    @JoinColumn(name = "accountId", referencedColumnName = "accountId")
    Account account;
}
