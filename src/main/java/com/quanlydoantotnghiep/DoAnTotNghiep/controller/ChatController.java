package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.chat.ChatMessageRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.chat.ChatMessageResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.chat.DeleteChatMessageRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    // sendMessage
    @MessageMapping("/sendMessage")
    public void sendMessage(ChatMessageRequest chatMessageRequest) {

        ChatMessageResponse chatMessageResponse = chatMessageService.saveChatMessage(chatMessageRequest);

        // send message to specific room (specific project):
        simpMessagingTemplate.convertAndSend("/topic/project."+chatMessageRequest.getProjectId(), chatMessageResponse);
    }

    // Revoke message
    @MessageMapping("/revokeMessage")
    public void revokeMessage(DeleteChatMessageRequest deleteChatMessageRequest) {

        ChatMessageResponse chatMessageResponse = chatMessageService.revokeMessage(deleteChatMessageRequest);

        // revoke message
        simpMessagingTemplate.convertAndSend("/topic/project."+deleteChatMessageRequest.getProjectId(), chatMessageResponse);
    }
}
