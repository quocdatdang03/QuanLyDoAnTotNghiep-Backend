//package com.quanlydoantotnghiep.DoAnTotNghiep.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//
//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.setApplicationDestinationPrefixes("/app"); // prefix for @MessageMapping("/sendMessage") -> /app/sendMessage
//        registry.enableSimpleBroker("/topic"); // prefix for subscribe channel -> /topic/messages
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        System.out.println("WebSocket endpoint registered: /ws");
//        registry.addEndpoint("/ws")
//                .setAllowedOriginPatterns("*")
//                .withSockJS();
//    }
//}
