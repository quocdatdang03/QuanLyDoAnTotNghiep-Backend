package com.quanlydoantotnghiep.DoAnTotNghiep.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiException extends RuntimeException{

     HttpStatus httpStatus;
     String message;
}
