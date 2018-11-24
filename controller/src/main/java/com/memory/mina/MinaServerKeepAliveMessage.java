package com.memory.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @program parent
 * @Author: cui.Memory
 * @Date: 2018/11/23 22:07
 * @description:
 */
@Component
public class MinaServerKeepAliveMessage implements KeepAliveMessageFactory {
    private static final Logger logger = LoggerFactory.getLogger(MinaServerKeepAliveMessage.class);
    /** 心跳包内容 */
    private static final String HEARTBEATREQUEST = "0x01";
    private static final String HEARTBEATRESPONSE = "0x02";

    @Override
    public boolean isRequest(IoSession ioSession, Object o) {
        //logger.info("请求心跳包信息: {}", o);
        if (o.equals(HEARTBEATREQUEST))
            return true;
        return false;
    }

    @Override
    public boolean isResponse(IoSession ioSession, Object o) {
        //logger.info("响应心跳包信息: {}", o);
        if(o.equals(HEARTBEATRESPONSE))
            return true;
        return false;
    }

    @Override
    public Object getRequest(IoSession ioSession) {
        //logger.info("请求预设信息: {}", HEARTBEATREQUEST);
        //return HEARTBEATREQUEST;
        return null;
    }
    @Override
    public Object getResponse(IoSession ioSession, Object o) {
        //logger.info("响应预设信息: {}", HEARTBEATRESPONSE);
        logger.info("server-getResponse: id={}", ioSession.getId());
        return HEARTBEATRESPONSE;
    }
}
