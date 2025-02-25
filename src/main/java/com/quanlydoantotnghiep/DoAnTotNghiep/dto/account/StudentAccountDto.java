package com.quanlydoantotnghiep.DoAnTotNghiep.dto.account;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.clazz.ClassDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentAccountDto {

    Long studentId;
    boolean isLeader;
    boolean isJoinedTeam;
    ClassDto studentClass;
}
