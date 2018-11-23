package com.memory.rabbitmq.utils;

import com.memory.rabbitmq.entity.IMMessage;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Auther: cui.Memory
 * @Date: 2018/11/20 0020 10:48
 * @Description:
 */
@Component
public class RabbitMQUtil {
    private final static String EX_CHANGE_SINGLE = "ex_change_single";
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private Channel channel;
    //获取通信管道
    @Bean
    public Channel getChannel(){
        return rabbitTemplate.getConnectionFactory().createConnection().createChannel(false);
    }
    //RabbitMQ初始化
    public void init() throws Exception{
        //创建单聊路由器
        channel.exchangeDeclare(EX_CHANGE_SINGLE, BuiltinExchangeType.TOPIC, true);
        //创建群聊路由器
        for (int i = 0; i < 100; i++) {
            channel.exchangeDeclare(""+i, BuiltinExchangeType.FANOUT, true);
        }
        //创建用户队列
        for (int i = 0; i < 100; i++) {
            channel.queueDeclare(""+i, true, false, false, null);
            //绑定用户队列
            channel.queueBind(""+i, "0", ""+i);
        }
    }
    //发送消息
    public Boolean send(IMMessage imMessage) throws Exception{
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId("im_" + imMessage.getId());
        rabbitTemplate.convertAndSend(imMessage.getTo_type().toLowerCase().equals("single") ? EX_CHANGE_SINGLE : imMessage.getTo(),
                imMessage.getTo_type().toLowerCase().equals("single") ? imMessage.getTo() : "",
                imMessage, correlationData);
        return true;
    }
    //创建群组
    public void createGroup(String groupId, String userId) throws Exception{
        channel.exchangeDeclare(groupId, BuiltinExchangeType.FANOUT, true);
    }
    //创建用户
    public void createUser(String userId) throws Exception{
        channel.queueDeclare(userId, true, false, false, null);
    }
    //加入群成员
    public void joinGroup(String groupId, String userId) throws Exception{
        channel.queueBind(userId, groupId, userId);
    }
    //移除群成员
    public void removeGroup(String groupId, String userId) throws Exception{
        channel.queueUnbind(userId, groupId, "");
    }
    //删除群，路由
    public void delGroup(String groupId) throws Exception{
        channel.exchangeDelete(groupId, true);
    }
    //删除用户，队列
    public void  delUser(String userId) throws Exception{
        channel.queueDelete(userId);
    }
}
