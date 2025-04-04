package com.quanlydoantotnghiep.DoAnTotNghiep.service.impl;

import com.quanlydoantotnghiep.DoAnTotNghiep.dto.DegreeDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.FacultyDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.AccountDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.account.response.TeacherAccountResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.chat.ChatMessageRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.chat.ChatMessageResponse;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.chat.DeleteChatMessageRequest;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.StageDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.dto.stage.StageStatusDto;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Account;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.ChatMessage;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.ProjectStage;
import com.quanlydoantotnghiep.DoAnTotNghiep.entity.Stage;
import com.quanlydoantotnghiep.DoAnTotNghiep.exception.ApiException;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.AccountRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.ChatMessageRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.repository.ProjectStageRepository;
import com.quanlydoantotnghiep.DoAnTotNghiep.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ProjectStageRepository projectStageRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<ChatMessageResponse> getAllChatMessageByProjectId(Long projectId) {

        List<ChatMessage> chatMessages = chatMessageRepository.findAllChatMessagesByProject(projectId);

        List<ChatMessageResponse> chatMessageResponses = chatMessages.stream().map(
                item -> {

                    return convertToChatMessageResponse(item);

                }
        ).collect(Collectors.toList());

        return chatMessageResponses;
    }

    @Override
    public ChatMessageResponse saveChatMessage(ChatMessageRequest chatMessageRequest) {

        ProjectStage projectStage = projectStageRepository.findByStageStageIdAndProjectProjectId(chatMessageRequest.getStageId(), chatMessageRequest.getProjectId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "ProjectStage is not exists with given stageId: "+chatMessageRequest.getStageId()+" and projectId: "+chatMessageRequest.getProjectId()));

        Account sender = accountRepository.findById(chatMessageRequest.getSenderId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Sender is not exists with given id: "+chatMessageRequest.getSenderId()));

        ChatMessage chatMessage = ChatMessage.builder()
                .projectStage(projectStage)
                .sender(sender)
                .content(chatMessageRequest.getContent())
                .timestamp(LocalDateTime.now())
                .parentMessage(chatMessageRequest.getParentMessageId()!=null ? chatMessageRepository.findById(chatMessageRequest.getParentMessageId()).orElse(null) : null)
                .flagDelete(false)
                .build();

        ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);

        // convert to ChatMessageResponse :
        return convertToChatMessageResponse(savedChatMessage);
    }

    @Override
    public ChatMessageResponse revokeMessage(DeleteChatMessageRequest revokeChatMessageRequest) {

        // validate ProjectStage
        ProjectStage projectStage = projectStageRepository.findByStageStageIdAndProjectProjectId(revokeChatMessageRequest.getStageId(), revokeChatMessageRequest.getProjectId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "ProjectStage is not exists with given stageId: "+revokeChatMessageRequest.getStageId()+" and projectId: "+revokeChatMessageRequest.getProjectId()));

        Account sender = accountRepository.findById(revokeChatMessageRequest.getSenderId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Sender is not exists with given id: "+revokeChatMessageRequest.getSenderId()));

        ChatMessage chatMessage = chatMessageRepository.findById(revokeChatMessageRequest.getChatMessageId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "ChatMessage is not exists with given id: "+revokeChatMessageRequest.getChatMessageId()));


        // check if sender is not own this chatMessage -> throw error
        if(!chatMessage.getSender().getAccountId().equals(sender.getAccountId()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Sender is not valid!");

        chatMessage.setFlagDelete(true);

        ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);

        // convert to ChatMessageResponse :
        return convertToChatMessageResponse(savedChatMessage);
    }

    private ChatMessageResponse convertToChatMessageResponse(ChatMessage chatMessage) {

        ChatMessageResponse parentMessage = null;
        if(chatMessage.getParentMessage()!=null)
        {
            parentMessage = ChatMessageResponse.builder()
                    .chatMessageId(chatMessage.getParentMessage().getChatMessageId())
                    .content(chatMessage.getParentMessage().getContent())
                    .sender(modelMapper.map(chatMessage.getParentMessage().getSender(), AccountDto.class))
                    .timestamp(chatMessage.getParentMessage().getTimestamp())
                    .stage(convertToStageDto(chatMessage.getParentMessage().getProjectStage().getStage(), chatMessage.getProjectStage()))
                    .isRevoked(chatMessage.getParentMessage().isFlagDelete())
                    .build();
        }

        return ChatMessageResponse.builder()
                .chatMessageId(chatMessage.getChatMessageId())
                .content(chatMessage.getContent())
                .sender(modelMapper.map(chatMessage.getSender(), AccountDto.class))
                .timestamp(chatMessage.getTimestamp())
                .parentMessage(
                        parentMessage
                )
                .stage(convertToStageDto(chatMessage.getProjectStage().getStage(), chatMessage.getProjectStage()))
                .isRevoked(chatMessage.isFlagDelete())
                .build();
    }

    private StageDto convertToStageDto(Stage item, ProjectStage projectStage) {

        StageDto stageDto = StageDto.builder()
                .stageId(item.getStageId())
                .stageName(item.getStageName())
                .stageTitle(item.getStageTitle())
                .stageContent(item.getStageContent())
                .stageOrder(item.getStageOrder())
                .stageStatus(modelMapper.map(projectStage.getStageStatus(), StageStatusDto.class))
                .build();

        TeacherAccountResponse teacherAccountResponse = modelMapper.map(item.getTeacher().getAccount(), TeacherAccountResponse.class);
        teacherAccountResponse.setTeacherCode(item.getTeacher().getAccount().getCode());
        teacherAccountResponse.setDegree(
                modelMapper.map(item.getTeacher().getDegree(), DegreeDto.class)
        );
        teacherAccountResponse.setFaculty(
                modelMapper.map(item.getTeacher().getFaculty(), FacultyDto.class)
        );
        teacherAccountResponse.setLeader(item.getTeacher().isLeader());
        stageDto.setTeacher(teacherAccountResponse);

        return stageDto;
    }
}
