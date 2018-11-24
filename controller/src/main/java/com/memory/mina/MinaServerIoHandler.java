package com.memory.mina;

import com.memory.rabbitmq.utils.RabbitMQUtil;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: cui.Memory
 * @Date: 2018/11/19 0019 13:44
 * @Description:
 */
@Component
public class MinaServerIoHandler extends IoHandlerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(MinaServerIoHandler.class);

    public static ConcurrentHashMap<String, IoSession> IoSessionCHM = new ConcurrentHashMap<String, IoSession>();

    @Autowired
    private RabbitMQUtil rabbitMQUtil;
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        //logger.info("server-sessionCreated");
        logger.info("sessionCHM: id={} - size={}", session.getId(), IoSessionCHM.get(""+session.getId()));
        IoSessionCHM.put(""+session.getId(), session);
        super.sessionCreated(session);
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        //logger.info("server-sessionOpened");
        super.sessionOpened(session);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        //logger.info("server-sessionClosed");
        logger.info("sessionClosed: size={}, id={} ", IoSessionCHM.size(), session.getId());
        IoSessionCHM.remove(session);
        logger.info("=sessionClosed: size={}",  IoSessionCHM.size());

        super.sessionClosed(session);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        //logger.info("server-sessionIdle");
        //session.closeNow();
        //***服务端处理断连session
        logger.info("server-心跳请求超时，与客户端断开连接: {}", session.getId());
        super.sessionIdle(session, status);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        //logger.info("server-exceptionCaught");
        super.exceptionCaught(session, cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        //logger.info("server-messageReceived: " + message.toString());
        super.messageReceived(session, message);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        //logger.info("server-messageSent: " + message.toString());
        super.messageSent(session, message);
    }
}
