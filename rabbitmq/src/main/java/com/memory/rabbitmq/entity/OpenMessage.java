package com.memory.rabbitmq.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @program parent
 * @Author: cui.Memory
 * @Date: 2018/11/28 19:54
 * @description:
 */
public class OpenMessage implements Serializable {
    private static final long serialVersionUID = 9141815410261662360L;
    private String uid;
    private String type;
    private Date date;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "OpenMessage{" +
                "uid='" + uid + '\'' +
                ", type='" + type + '\'' +
                ", date=" + date +
                '}';
    }
}
