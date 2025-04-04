package com.quanlydoantotnghiep.DoAnTotNghiep.service;

import java.util.List;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.chat.ChatMessageRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.chat.ChatMessageResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.chat.DeleteChatMessageRequest;

public interface ChatMessageService {

    List<ChatMessageResponse> getAllChatMessageByProjectId(Long projectId);
    ChatMessageResponse saveChatMessage(ChatMessageRequest chatMessageRequest);
    ChatMessageResponse revokeMessage(DeleteChatMessageRequest revokeChatMessageRequest);

}
