package com.quanlydoantotnghiep.DoAnTotNghiep.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "ChatMessages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long chatMessageId;

    @Column(columnDefinition = "TEXT", nullable = false)
    String content;

    @Column(nullable = false)
    LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "chatRoomId", nullable = false)
    ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "senderId", referencedColumnName = "accountId", nullable = false)
    Account sender;

    @ManyToOne
    @JoinColumn(name = "parentChatMessageId", referencedColumnName = "chatMessageId", nullable = true)
    ChatMessage parentMessage;

    boolean isDeleted;
}
