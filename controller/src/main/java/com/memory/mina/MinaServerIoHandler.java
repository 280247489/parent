package com.memory.mina;

import com.memory.rabbitmq.entity.CloseMessage;
import com.memory.rabbitmq.entity.IMMessage;
import com.memory.rabbitmq.entity.OpenMessage;
import com.memory.rabbitmq.utils.RabbitMQUtil;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        logger.info("sessionCreated: sessionCount: {}", session.getService().getManagedSessionCount());
     }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        clearRabbitMQ(session);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status);
        logger.info("sessionIdle-心跳请求超时: userId: {} - sessionCount: {}", session.getId(), session.getService().getManagedSessionCount());
        session.closeOnFlush();
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        //super.exceptionCaught(session, cause);
        logger.info("exceptionCaught: userId: {} - sessionCount: {} - 异常: ",
                session.getAttribute("uid").toString(),
                session.getService().getManagedSessionCount(),
                cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message);
        doRabbitMQ(session, message);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
    }

    private void doRabbitMQ(IoSession session, Object message) throws Exception {
        if(message instanceof IMMessage){
            rabbitMQUtil.send((IMMessage)message);
            logger.info("messageReceived-im: {}", ((IMMessage)message).toString());
        }else if(message instanceof OpenMessage){
            OpenMessage openMessage = (OpenMessage) message;
            String consumerTag = new StringBuffer(openMessage.getType()
                    + "-" + openMessage.getUid()).toString();
            if(!MinaSessionMap.getMap().containsKey(consumerTag)){
                openPut(session, openMessage, consumerTag);
                logger.info("messageReceived-open: {}", consumerTag);
            }else{
                session.write("repeat con");
                session.closeOnFlush();
                logger.info("messageReceived-open-exist: {}", consumerTag);
            }
        }else if(message instanceof CloseMessage){
            session.closeOnFlush();
            logger.info("messageReceived-close: {}", ((CloseMessage)message).getUid());
        }
    }

    private void clearRabbitMQ(IoSession session) {
        if(session.getAttribute("uid")!=null){
            closeRemove(session);
        }
        logger.info("sessionClosed: userId: {} - sessionCount: {}", session.getAttribute("uid"), session.getService().getManagedSessionCount());
    }

    private void openPut(IoSession session, OpenMessage openMessage, String consumerTag) {
        session.setAttribute("uid", openMessage.getUid());
        session.setAttribute("type", openMessage.getType());
        MinaSessionMap.getMap().put(consumerTag, session);
        rabbitMQUtil.open(session);
    }

    private void closeRemove(IoSession session) {
        String consumerTag = new StringBuffer(
                session.getAttribute("type" ) + "-" +
                        session.getAttribute("uid" )).toString();
        MinaSessionMap.getMap().remove(consumerTag);
        rabbitMQUtil.close(consumerTag);
        logger.info("MinaSessionMap = {}", MinaSessionMap.getMap().size());

    }
}
