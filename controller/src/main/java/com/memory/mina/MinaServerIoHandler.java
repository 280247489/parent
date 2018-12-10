package com.memory.mina;

import com.memory.mina.entity.CloseMessage;
import com.memory.mina.entity.IMMessage;
import com.memory.mina.entity.OpenMessage;
import com.memory.mina.entity.SysMessage;
import com.memory.rabbitmq.utils.RabbitMQUtil;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
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
     }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        String consumerTag = new StringBuffer(
                session.getAttribute("type" ) + "-" +
                        session.getAttribute("uid" )).toString();
        if(!"repeat".equals(session.getAttribute("closeInfo"))){
            doSessionClosed(consumerTag);
        }
        logger.info("sessionClosed: info: {} - userId: {} - count: {} - cache: {}",
                session.getAttribute("closeInfo"),
                consumerTag,
                session.getService().getManagedSessionCount(),
                MinaSessionCache.getMap().size());
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status);
        String consumerTag = new StringBuffer(session.getAttribute("type")
                + "-" + session.getAttribute("uid")).toString();
//        logger.info("sessionIdle-心跳请求超时: userId: {} ", consumerTag);
        session.setAttribute("closeInfo", "sessionIdle");
        session.closeOnFlush();
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
        String consumerTag = new StringBuffer(session.getAttribute("type")
                + "-" + session.getAttribute("uid")).toString();
//        logger.info("exceptionCaught: userId: {}", consumerTag);
        session.setAttribute("closeInfo", "exceptionCaught");
        session.closeOnFlush();
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message);
        //处理客户端消息
        doMessage(session, message);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
    }

    private void doMessage(IoSession session, Object message) throws Exception {
        if(message instanceof IMMessage){
            doIMMessage((IMMessage) message);
        }else if(message instanceof OpenMessage){
            doOpenMessage(session, (OpenMessage) message);
        }else if(message instanceof CloseMessage){
            doCloseMessage(session, (CloseMessage) message);
        }
    }

    private void doIMMessage(IMMessage message) throws Exception {
        IMMessage imMessage = message;
        logger.info("messageReceived-imMessage");
        //发送消息
        rabbitMQUtil.send(imMessage.getToType(), imMessage.getToId(), imMessage);
    }

    private void doOpenMessage(IoSession session, OpenMessage message) {
        OpenMessage openMessage = message;
        String consumerTag = new StringBuffer(openMessage.getType()
                + "-" + openMessage.getUid()).toString();
        if(MinaSessionCache.getMap().containsKey(consumerTag)){
            IoSession oldIoSession = MinaSessionCache.getMap().get(consumerTag);
            SysMessage sysMessage = new SysMessage();
            sysMessage.setSysType("exit");
            sysMessage.setContent("账户另一地点登录，强制退出。如非本人操作，请联系管理员！");
            oldIoSession.write(sysMessage);
            //处理关闭ioSession
            oldIoSession.setAttribute("closeInfo", "repeat");
            doSessionClosed(consumerTag);
            oldIoSession.closeOnFlush();
        }
        session.setAttribute("uid", openMessage.getUid());
        session.setAttribute("type", openMessage.getType());
        MinaSessionCache.getMap().put(consumerTag, session);
        //处理消息，如RabbitMQ，需要创建绑定消费者
        rabbitMQUtil.open(session);
        logger.info("messageReceived-openMessage: {}", consumerTag);
    }

    private void doCloseMessage(IoSession session, CloseMessage message) {
        CloseMessage closeMessage = message;
        //处理关闭ioSession
        session.setAttribute("closeInfo", "closeMessage");
        session.closeOnFlush();
    }

    private void doSessionClosed(String consumerTag) {
        //处理cache以及RabbitMQ消费者
        MinaSessionCache.getMap().remove(consumerTag);
        rabbitMQUtil.close(consumerTag);
    }
}
