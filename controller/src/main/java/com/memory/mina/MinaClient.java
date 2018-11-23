package com.memory.mina;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringCodecFactory;
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

    public static void main(String[] args) {
        IoConnector connector = new NioSocketConnector();
        connector.getFilterChain().addLast("logger", new LoggingFilter());
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new PrefixedStringCodecFactory(Charset.forName("UTF-8"))));
        connector.setHandler(new MinaClientHandler());
        try {
            ConnectFuture future = connector.connect(new InetSocketAddress("::", PORT));
            future.awaitUninterruptibly();
            IoSession session = future.getSession();
            System.out.println("mina-client-success");
            //session.write("init");
            session.write("del");
        } catch (Exception e) {
            System.err.println("连接失败");
            e.printStackTrace();
        }
    }
}
