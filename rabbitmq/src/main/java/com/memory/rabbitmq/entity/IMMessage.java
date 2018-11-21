package com.memory.rabbitmq.entity;

import java.io.Serializable;

/**
 * @Auther: cui.Memory
 * @Date: 2018/11/7 0007 9:18
 * @Description:
 */
public class IMMessage implements Serializable{
    private static final long serialVersionUID = 8652447851744777346L;
    private String id;
    private String content;

    public IMMessage() {
    }

    public IMMessage(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
