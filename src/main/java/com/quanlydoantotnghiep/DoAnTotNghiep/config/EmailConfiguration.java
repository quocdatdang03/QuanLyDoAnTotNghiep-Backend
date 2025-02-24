package com.quanlydoantotnghiep.DoAnTotNghiep.config;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailConfiguration {

    @Value("${spring.mail.host}")
    String mailHost;

    @Value("${spring.mail.port}")
    String mailPort;

    @Value("${spring.mail.username}")
    String emailUserName;

    @Value("${spring.mail.password}")
    String password;

    @Bean
    public JavaMailSender javaMailSender() {

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(mailHost);
        javaMailSender.setPort(Integer.parseInt(mailPort));
        javaMailSender.setUsername(emailUserName);
        javaMailSender.setPassword(password);

        Properties properties = javaMailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug", "true");

        javaMailSender.setJavaMailProperties(properties);

        return javaMailSender;
    }
}
