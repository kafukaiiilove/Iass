package com.example.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 公告信息表
*/
public class A13 implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;
    /** 标题功能描述 */
    private String title;
    /** 方法属性键名 */
    private String content;
    /** 示例参数 */
    private String parameter;
    /** 参数描述 */
    private String description;
    /** 创建时间 */
    private LocalDateTime time;
    /** 创建人 */
    private String user;
    


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}