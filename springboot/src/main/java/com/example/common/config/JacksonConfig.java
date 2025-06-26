package com.example.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        
        // 创建JavaTimeModule来处理Java 8时间类型
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        
        // 定义多种时间格式
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        DateTimeFormatter formatter4 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        
        // 注册序列化器
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter1));
        
        // 注册反序列化器，支持多种格式
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter1) {
            @Override
            public LocalDateTime deserialize(com.fasterxml.jackson.core.JsonParser p, com.fasterxml.jackson.databind.DeserializationContext ctxt) throws java.io.IOException {
                String text = p.getText();
                if (text == null || text.trim().isEmpty()) {
                    return null;
                }
                
                // 尝试多种格式解析
                try {
                    return LocalDateTime.parse(text, formatter1);
                } catch (Exception e1) {
                    try {
                        return LocalDateTime.parse(text, formatter2);
                    } catch (Exception e2) {
                        try {
                            return LocalDateTime.parse(text, formatter3);
                        } catch (Exception e3) {
                            try {
                                return LocalDateTime.parse(text, formatter4);
                            } catch (Exception e4) {
                                throw new RuntimeException("无法解析时间格式: " + text + 
                                    "，支持的格式: yyyy-MM-dd HH:mm:ss, yyyy-MM-dd HH:mm, yyyy-MM-ddTHH:mm:ss, yyyy-MM-ddTHH:mm:ss.SSS", e4);
                            }
                        }
                    }
                }
            }
        });
        
        objectMapper.registerModule(javaTimeModule);
        
        return objectMapper;
    }
} 