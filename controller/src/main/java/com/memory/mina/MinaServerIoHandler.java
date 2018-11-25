package com.memory.mina;

import com.memory.rabbitmq.utils.RabbitMQUtil;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: cui.Memory
 * @Date: 2018/11/19 0019 13:44
 * @Description:
 */
@Component
public class MinaServerIoHandler extends IoHandlerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(MinaServerIoHandler.class);

    @Autowired
    private RabbitMQUtil rabbitMQUtil;
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);

        //测试数据
        session.setAttribute("userId", session.getId());
        session.setAttribute("groupId", "group1");

        String userId = session.getAttribute("userId").toString();
        String groupId = session.getAttribute("groupId").toString();

        logger.info("sessionCreated: userId: {} - sessionCount: {}", userId, session.getService().getManagedSessionCount());
        rabbitMQUtil.createUser(userId);
        rabbitMQUtil.createGroup(groupId);
        rabbitMQUtil.joinGroup(groupId, userId);
        rabbitMQUtil.consumeStart(session, userId);
     }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);

        logger.info("sessionClosed: userId: {} - sessionCount: {}", session.getId(), session.getService().getManagedSessionCount());
        rabbitMQUtil.consumeEnd(""+session.getId());
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status);

        logger.info("sessionIdle-心跳请求超时: userId: {} - sessionCount: {}", session.getId(), session.getService().getManagedSessionCount());
        session.closeOnFlush();
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
    }
}
