package com.memory.mina;

import org.apache.mina.core.session.IoSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: cui.Memory
 * @Date: 2018/11/24 0024 11:27
 * @Description:
 */
public class MinaSessionMap {
    private static Map<String, IoSession> map = new ConcurrentHashMap();
    public static Map<String, IoSession> getMap() {
        return map;
    }
}
