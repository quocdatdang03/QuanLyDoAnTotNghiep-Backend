package com.quanlydoantotnghiep.DoAnTotNghiep.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorDetails {

    LocalDateTime localDateTime;
    String message;
    String details;

}
