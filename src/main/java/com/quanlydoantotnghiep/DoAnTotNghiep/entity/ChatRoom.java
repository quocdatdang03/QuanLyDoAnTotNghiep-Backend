//package com.quanlydoantotnghiep.DoAnTotNghiep.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import lombok.experimental.FieldDefaults;
//
//import java.util.Set;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@FieldDefaults(level = AccessLevel.PRIVATE)
//@Entity
//@Table(name = "PHONGCHAT")
//public class ChatRoom {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "maPhongChat")
//    Long chatRoomId;
//
//    @OneToOne
//    @JoinColumn(name = "maDeTai", nullable = false, unique = true)
//    Project project;
//
//    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
//    Set<ChatMessage> chatMessages;
//
//}
