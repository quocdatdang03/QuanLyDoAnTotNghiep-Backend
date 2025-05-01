package com.quanlydoantotnghiep.DoAnTotNghiep.entity;

import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Embeddable
public class StudentTeacherProposalId implements Serializable {

    Long studentId;
    Long teacherId;
    Long semesterId;
}
