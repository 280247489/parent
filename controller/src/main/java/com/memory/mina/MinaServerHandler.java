package com.memory.mina;

import com.memory.rabbitmq.utils.RabbitMQUtil;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Auther: cui.Memory
 * @Date: 2018/11/19 0019 13:44
 * @Description:
 */
public class MinaServerHandler extends IoHandlerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(MinaServerHandler.class);
    @Autowired
    private RabbitMQUtil rabbitMQUtil;
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        logger.info("server-sessionCreated");
        super.sessionCreated(session);
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        logger.info("server-sessionOpened");
        super.sessionOpened(session);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        logger.info("server-sessionClosed");
        super.sessionClosed(session);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        logger.info("server-sessionIdle");
        super.sessionIdle(session, status);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        logger.info("server-exceptionCaught");
        super.exceptionCaught(session, cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        logger.info("server-messageReceived: " + message.toString());
        super.messageReceived(session, message);
        if(message.toString().equals("init")){
            logger.info("RabbitMQ - init");
            rabbitMQUtil.init();
            logger.info("RabbitMQ - init - end");
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        logger.info("server-messageSent: " + message.toString());
        super.messageSent(session, message);
    }
}
