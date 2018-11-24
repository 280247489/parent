package com.memory.mina.client;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringCodecFactory;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @Auther: cui.Memory
 * @Date: 2018/11/19 0019 8:40
 * @Description:
 */
public class MinaClient {

    private static final int PORT = 9123;
    private static final int IDELTIME = 30;
    private static final int IDELTIMEOUT = 30;
    private static final int HEARTBEATRATE = 60;
    public static void main(String[] args) {
        IoConnector connector = new NioSocketConnector();
        connector.getFilterChain().addLast("logger", new LoggingFilter());
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(
                new PrefixedStringCodecFactory(Charset.forName("UTF-8"))));

        KeepAliveFilter heartBeat = new KeepAliveFilter(new MinaClientKeepAliveMessage(), IdleStatus.BOTH_IDLE,
                new MinaClientKeepAliveRequestTimeoutHandler());
        connector.getFilterChain().addLast("heartBeat", heartBeat);
        //设置是否forward到下一个filter
        heartBeat.setForwardEvent(true);
        //设置心跳频率
        heartBeat.setRequestInterval(HEARTBEATRATE);
        heartBeat.setRequestTimeout(IDELTIMEOUT);
        connector.setHandler(new MinaClientHandler());
        try {
            ConnectFuture future = connector.connect(new InetSocketAddress("::", PORT));
            future.awaitUninterruptibly();
            IoSession session = future.getSession();
            session.write("hey");
        } catch (Exception e) {
            System.err.println("连接失败");
            e.printStackTrace();
        }
    }
}
