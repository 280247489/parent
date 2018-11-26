package com.memory.mina.utils;

import java.io.Serializable;

/**
 * @Auther: cui.Memory
 * @Date: 2018/11/26 0026 15:31
 * @Description:
 */
public class FirstConMsg implements Serializable {
    private static final long serialVersionUID = -9025356323795852606L;
    private String userId;
    private String time;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "FirstConMsg{" +
                "userId='" + userId + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
