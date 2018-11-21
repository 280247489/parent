package com.memory.rabbitmq;

import com.memory.rabbitmq.entity.IMMessage;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Auther: cui.Memory
 * @Date: 2018/11/7 0007 9:59
 * @Description:
 */
@Component
public class IMReceiver {
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(name = "im-exchange-single", type = "topic"),
            value = @Queue(value = "im-queue-1", durable = "true"),
            key = "im.userId.1"
        )
    )
    @RabbitHandler
    public void onIMMessage1(@Payload IMMessage imMessage,
                             @Headers Map<String, Object> headers,
                             Channel channel) throws Exception{
        //消费者操作
        System.out.println("1----------收到IM消息,开始消费----------");
        System.out.println("消息content: " + imMessage.getContent());
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliveryTag, false);
    }


    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(name = "im-exchange-single", type = "topic"),
            value = @Queue(value = "im-queue-2", durable = "true"),
            key = "im.userId.2"
        )
    )
    @RabbitHandler
    public void onIMMessage2(@Payload IMMessage imMessage,
                            @Headers Map<String, Object> headers,
                            Channel channel) throws Exception{
        //消费者操作
        System.out.println("2----------收到IM消息,开始消费----------");
        System.out.println("消息content: " + imMessage.getContent());
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliveryTag, false);
    }


}
