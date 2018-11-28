package com.memory.mina;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringCodecFactory;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @Auther: cui.Memory
 * @Date: 2018/11/19 0019 8:40
 * @Description:
 */
@Component
public class MinaServer {
    private static final Logger logger = LoggerFactory.getLogger(MinaServer.class);
    private static final int PORT = 9123;
    private static final int IDELTIME = 30;
    private static final int IDELTIMEOUT = 20;
    private static final int HEARTBEATRATE = 90;
    @Autowired
    private MinaServerIoHandler minaServerIoHandler;
    @Autowired
    private MinaServerKeepAliveMessage minaServerKeepAliveMessage;
    @Autowired
    private MinaServerKeepAliveRequestTimeoutHandler minaServerKeepAliveRequestTimeoutHandler;
    @Bean
    public void startup() {
        try {
            NioSocketAcceptor acceptor = new NioSocketAcceptor();
            acceptor.setHandler(minaServerIoHandler);
            acceptor.getSessionConfig().setReadBufferSize(2048);
            //acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, IDELTIME);
            acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(
                    new ObjectSerializationCodecFactory()));
            //acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(
            //        new PrefixedStringCodecFactory(Charset.forName("UTF-8"))));
            acceptor.getFilterChain().addLast("logger", new LoggingFilter());

            KeepAliveFilter heartBeat = new KeepAliveFilter(minaServerKeepAliveMessage, IdleStatus.READER_IDLE);
            //设置是否forward到下一个filter
            heartBeat.setForwardEvent(true);
            //设置心跳频率
            heartBeat.setRequestInterval(HEARTBEATRATE);
            //heartBeat.setRequestTimeout(IDELTIMEOUT);
            acceptor.getFilterChain().addLast("heartbeat", heartBeat);
            acceptor.bind(new InetSocketAddress(PORT));

            logger.info("\nmina-server-start: {}", PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
