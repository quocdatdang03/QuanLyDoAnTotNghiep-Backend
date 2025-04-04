package com.quanlydoantotnghiep.DoAnTotNghiep.dto.chat;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeleteChatMessageRequest {

    Long senderId;
    Long chatMessageId;
    Long projectId;
    Long stageId;
}