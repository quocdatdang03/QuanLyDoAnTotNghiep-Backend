package com.quanlydoantotnghiep.DoAnTotNghiep.controller;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.chat.ChatMessageResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatMessages")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    // get all messages by projectId
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<ChatMessageResponse>> getChatMessagesByProjectId(
            @PathVariable("projectId") Long projectId
    ) {

        return ResponseEntity.ok(chatMessageService.getAllChatMessageByProjectId(projectId));
    }
}
