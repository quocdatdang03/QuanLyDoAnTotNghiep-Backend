package com.quanlydoantotnghiep.DoAnTotNghiep.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ObjectResponse<T> {

    List<T> content;
    int pageNumber;
    int pageSize;
    long totalElements;
    long totalPages;
    boolean isLast;
}
