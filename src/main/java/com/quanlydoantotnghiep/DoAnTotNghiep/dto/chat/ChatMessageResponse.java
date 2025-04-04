package com.quanlydoantotnghiep.DoAnTotNghiep.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.StageDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessageResponse {

    Long chatMessageId;
    AccountDto sender;
    String content;
    LocalDateTime timestamp;
    StageDto stage;
    ChatMessageResponse parentMessage;

    @JsonProperty("isRevoked")
    boolean isRevoked;
}
