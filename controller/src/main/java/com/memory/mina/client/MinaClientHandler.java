package com.memory.mina.client;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: cui.Memory
 * @Date: 2018/11/19 0019 13:51
 * @Description:
 */
public class MinaClientHandler extends IoHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(MinaClientHandler.class);

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        //logger.info("client-sessionCreated");
        super.sessionCreated(session);
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        //logger.info("client-sessionOpened");
        super.sessionOpened(session);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        //logger.info("client-sessionClosed");
        super.sessionClosed(session);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        //logger.info("client-sessionIdle");
        super.sessionIdle(session, status);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        //logger.info("client-exceptionCaught");
        super.exceptionCaught(session, cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        //logger.info("client-messageReceived: {}", message.toString());
        super.messageReceived(session, message);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        //logger.info("client-messageSent: {}", message.toString());
        super.messageSent(session, message);
    }
}
