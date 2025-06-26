package com.example.entity;

import lombok.Data;

@Data
public class NoticeMarkdown {
    private Integer id;
    private String title;
    private String content;
    private String parameter;
    private String description;
    private String time;
    private String user;
    private String filePath;
} 