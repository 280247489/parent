package com.memory.mina.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @program parent
 * @Author: cui.Memory
 * @Date: 2018/11/28 19:57
 * @description:
 */
public class IMMessage implements Serializable {
    private static final long serialVersionUID = 1672637166632693038L;
    private String id;          //消息ID
    private int type;           //1文本，2图片，3语音，4视频，5系统
    //1txt-text, 2pic-picture, 3voi-voice, 4vid-video, 5sys-system
    private String content;     //消息内容
    private Date date;          //消息发送时间
    private String from;        //发送人
    private String toType;     //接收类型 group群，singel单聊
    private String toId;          //接收人

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

    public String getToType() {
        return toType;
    }

    public void setToType(String toType) {
        this.toType = toType;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    @Override
    public String toString() {
        return "IMMessage{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", date=" + date +
                ", from='" + from + '\'' +
                ", toType='" + toType + '\'' +
                ", toId='" + toId + '\'' +
                '}';
    }
}
