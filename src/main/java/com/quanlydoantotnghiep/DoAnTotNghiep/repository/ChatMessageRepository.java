package com.quanlydoantotnghiep.DoAnTotNghiep.repository;

import com.quanlydoantotnghiep.DoAnTotNghiep.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

//    List<ChatMessage> findByChatRoom_ChatRoomId(Long chatRoomId);

    @Query("""
        SELECT c FROM ChatMessage c
            WHERE c.projectStage.project.projectId = :projectId
    """)
    List<ChatMessage> findAllChatMessagesByProject(@Param("projectId") Long projectId);
}
