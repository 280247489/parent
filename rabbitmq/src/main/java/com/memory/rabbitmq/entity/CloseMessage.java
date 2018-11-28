package com.memory.rabbitmq.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @program parent
 * @Author: cui.Memory
 * @Date: 2018/11/28 19:55
 * @description:
 */
public class CloseMessage implements Serializable {
    private static final long serialVersionUID = 7613028390325773474L;
    private String uid;
    private Date date;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "CloseMessage{" +
                "uid='" + uid + '\'' +
                ", date=" + date +
                '}';
    }
}
