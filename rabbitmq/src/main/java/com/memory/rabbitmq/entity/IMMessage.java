package com.memory.rabbitmq.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: cui.Memory
 * @Date: 2018/11/7 0007 9:18
 * @Description:
 */
public class IMMessage implements Serializable{
    private static final long serialVersionUID = 8652447851744777346L;
    private String id;          //消息ID
    private int type;           //1文本，2图片，3语音，4视频
    private String content;     //消息内容
    private Date date;          //消息发送时间
    private String from;        //发送人
    private String to_type;     //接受类型 group群，singel单聊
    private String to;          //接收人
    public IMMessage() {
    }

    public IMMessage(String id, int type, String content, Date date, String from, String to_type, String to) {
        this.id = id;
        this.type = type;
        this.content = content;
        this.date = date;
        this.from = from;
        this.to_type = to_type;
        this.to = to;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo_type() {
        return to_type;
    }

    public void setTo_type(String to_type) {
        this.to_type = to_type;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "IMMessage{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", date=" + date +
                ", from='" + from + '\'' +
                ", to_type='" + to_type + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
