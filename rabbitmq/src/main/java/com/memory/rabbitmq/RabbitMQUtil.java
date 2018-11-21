package com.memory.rabbitmq;

import com.memory.rabbitmq.entity.IMMessage;
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
    //创建路由交换机
    public void createExChange(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.
    }
    //创建消息队列
    //绑定消息队列
}
