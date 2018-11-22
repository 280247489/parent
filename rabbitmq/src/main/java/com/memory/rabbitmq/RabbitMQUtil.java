package com.memory.rabbitmq;

import com.memory.rabbitmq.entity.IMMessage;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: cui.Memory
 * @Date: 2018/11/20 0020 10:48
 * @Description:
 */
@Component
public class RabbitMQUtil {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Boolean send(String routingKey, IMMessage imMessage) throws Exception{
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId("im_" + imMessage.getId());
        rabbitTemplate.convertAndSend("im-exchange-single", routingKey, imMessage, correlationData);
        return true;
    }
    //获取通信管道
    public Channel getChannel(){
        return rabbitTemplate.getConnectionFactory().createConnection().createChannel(false);
    }
    //创建群组
    public void createGroup(Channel channel, String groupId) throws Exception{
        channel.exchangeDeclare(groupId, BuiltinExchangeType.FANOUT);
    }
    //创建用户
    public void createUser(Channel channel, String userId) throws Exception{
        channel.queueDeclare(userId, false, false, false, null);
    }
    //加入群成员
    public void joinGroup(Channel channel, String groupId, String userId) throws Exception{
        channel.queueBind(userId, groupId, "");
    }
    //移除群成员
    public void removeGroup(Channel channel, String groupId, String userId) throws Exception{
        channel.queueUnbind(userId, groupId, "");
    }
    //删除群
    public void deleteGroup(Channel channel, String groupId) throws Exception{
        channel.exchangeDelete(groupId, true);
    }
}
