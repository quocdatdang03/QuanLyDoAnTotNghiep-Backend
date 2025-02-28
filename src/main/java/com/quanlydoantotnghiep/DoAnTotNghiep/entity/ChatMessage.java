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
@Table(name = "TINNHAN")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maTinNhan")
    Long chatMessageId;

    @Column(name = "noiDung",columnDefinition = "TEXT", nullable = false)
    String content;

    @Column(name = "thoiGianGui",nullable = false)
    LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "maPhongChat", nullable = false)
    ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "maNguoiGui", referencedColumnName = "maTaiKhoan", nullable = false)
    Account sender;

    @ManyToOne
    @JoinColumn(name = "maTinNhanGoc", referencedColumnName = "maTinNhan", nullable = true)
    ChatMessage parentMessage;

    boolean isDeleted;
}
