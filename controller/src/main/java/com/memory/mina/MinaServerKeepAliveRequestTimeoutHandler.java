package com.memory.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @program parent
 * @Author: cui.Memory
 * @Date: 2018/11/23 22:16
 * @description:
 */
@Component
public class MinaServerKeepAliveRequestTimeoutHandler implements KeepAliveRequestTimeoutHandler {
    private static final Logger logger = LoggerFactory.getLogger(MinaServerKeepAliveRequestTimeoutHandler.class);

    @Override
    public void keepAliveRequestTimedOut(KeepAliveFilter keepAliveFilter, IoSession ioSession) throws Exception {
        ioSession.closeNow();
        logger.info("心跳请求超时，断开连接");
    }
}
