package com.memory.rabbitmq.utils;

import com.memory.rabbitmq.entity.IMMessage;
import com.rabbitmq.client.*;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Auther: cui.Memory
 * @Date: 2018/11/20 0020 10:48
 * @Description:
 */
@Component
public class RabbitMQUtil {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQUtil.class);
    private final static String EX_CHANGE_SINGLE = "ex_change_single";
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private Connection connection;
    @Autowired
    private Channel channel;

    //获取通信桥连接
    @Bean
    public Connection getConnection(){
        return rabbitTemplate.getConnectionFactory().createConnection();
    }
    //获取通信管道
    @Bean
    public Channel getChannel(){
        return connection.createChannel(false);
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
    //发送消息-test
    public Boolean send(String msg) throws Exception{
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId("id");
        rabbitTemplate.convertAndSend("group1", "", msg, correlationData);
        return true;
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
    //开始消费消息
    public void consumeStart(final IoSession ioSession, String userId) throws Exception{
        channel.basicConsume(userId, false, "consumer_"+ userId,
                new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope,
                                               AMQP.BasicProperties properties,
                                               byte[] body) throws IOException {
                        // 捕获消息内容
                        String message = new String(body, "UTF-8");
                        //消息处理（自己实现的方法）
                        ioSession.write(message);
                        //消息确认
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                });
    }
    //结束消费
    public void consumeEnd(String userId) throws Exception{
        channel.basicCancel("consumer_"+userId);
        logger.info("注销消费者: consumer_{}", userId);
    }
    //创建群组
    public void createGroup(String groupId) throws Exception{
        channel.exchangeDeclare(groupId, BuiltinExchangeType.FANOUT, true);
    }
    //创建用户
    public void createUser(String userId) throws Exception{
        //创建队列的参数
        /*
        Map<String, Object> queueArgs = new HashMap<String, Object>();
        queueArgs.put("x-dead-letter-exchange", "refreshDispatcherDeadExchange");  //死信队列
        queueArgs.put("x-message-ttl", 10000);     // 消息超时：让发布的message在队列中可以存活多长时间，以毫秒为单位。
        queueArgs.put("x-expires", 1000);          // 队列超时：当前的queue在指定的时间内，没有消费者订阅就会被删除，以毫秒为单位。
        queueArgs.put("x-max-length", 100);        // 队列最大长度：当超过了这个大小的时候，会删除之前最早插入的消息为本次的留出空间。
        queueArgs.put("x-queue-mode", "lazy");     //延迟加载：queue的信息尽可能的都保存在磁盘上，仅在有消费者订阅的时候才会加载到RAM中。
        */
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
