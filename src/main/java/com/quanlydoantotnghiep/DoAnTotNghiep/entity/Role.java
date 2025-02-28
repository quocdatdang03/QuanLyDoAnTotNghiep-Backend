package com.quanlydoantotnghiep.DoAnTotNghiep.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name="VAITRO")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maVaiTro")
    Long roleId;

    @Column(name = "tenVaiTro", columnDefinition = "varchar(50)", nullable = false, unique = true)
    String roleName;

    @ManyToMany(mappedBy = "roles")
    Set<Account> accounts = new HashSet<>();

}
