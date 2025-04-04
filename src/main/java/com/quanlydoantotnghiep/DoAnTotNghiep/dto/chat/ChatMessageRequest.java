package com.quanlydoantotnghiep.DoAnTotNghiep.dto.chat;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessageRequest {

    Long projectId;
    Long stageId;
    Long senderId;
    String content;
    Long parentMessageId;

}
