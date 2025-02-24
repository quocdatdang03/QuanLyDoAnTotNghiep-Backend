package com.quanlydoantotnghiep.DoAnTotNghiep.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name="Accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long accountId;

    @Column(columnDefinition = "varchar(255)", nullable = false, unique = true)
    String email;

    @Column(columnDefinition = "varchar(255)", nullable = false)
    String password;

    @Column(columnDefinition = "varchar(50)", nullable = false, unique = true)
    private String code;

    @Column(columnDefinition = "nvarchar(50)", nullable = false)
    private String fullName;

    @Column(nullable = false)
    LocalDate dateOfBirth;

    @Column(columnDefinition = "varchar(50)", nullable = false, unique = true)
    String phoneNumber;

    @Column(nullable = false)
    boolean gender;

    @Column(columnDefinition = "varchar(50)", nullable = false)
    String address;

    @Column(columnDefinition = "text", nullable = true)
    String image;


    // Chú ý để fetch là EAGER ở field roles của Entity User để tránh error LazyInitializationException
    // -> Dữ liệu về roles của user sẽ được tải ngay lập tức cùng với user (E.g. userRepository.findByEmail(email); thì nó sẽ select join ể lấy dữ liệu từ cả hai bảng User và Role)
    // Mặc định @ManyToMany có fetch là LAZY : tức là chỉ tải user khi load user còn khi thực sự gọi roles nó mới load roles
    // (E.g. userRepository.findByEmail(email); thì nó sẽ chỉ select từ table user, chỉ khi ta gọi user.getRoles(); thì nó mới load từ table roles)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "Account_Role",
            joinColumns = @JoinColumn(name = "accountId"),
            inverseJoinColumns = @JoinColumn(name="roleId")
    )
    Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "account")
    Student student;

    @OneToOne(mappedBy = "account")
    Teacher teacher;

    String resetPasswordVerificationCode;

    LocalDateTime resetPasswordVerificationCodeExpiredAt;

    boolean enable;

    boolean flagDelete;
}
