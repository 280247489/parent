package com.memory.rabbitmq;

import com.memory.rabbitmq.entity.IMMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @Auther: cui.Memory
 * @Date: 2018/11/7 0007 9:38
 * @Description:
 */
@Component
public class IMsender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Boolean send(String routingKey, IMMessage imMessage) throws Exception{
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId("im_" + imMessage.getId());
        rabbitTemplate.convertAndSend("im-exchange-single", routingKey, imMessage, correlationData);
        return true;
    }
}
