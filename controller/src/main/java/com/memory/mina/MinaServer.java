package com.memory.mina;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @Auther: cui.Memory
 * @Date: 2018/11/19 0019 8:40
 * @Description:
 */
public class MinaServer {

    private static final int PORT = 9123;

    public static void main(String[] args) {
        try {
            NioSocketAcceptor acceptor = new NioSocketAcceptor();
            acceptor.getFilterChain().addLast("logger", new LoggingFilter());
            acceptor.getFilterChain().addLast("codec",  new ProtocolCodecFilter( new PrefixedStringCodecFactory(Charset.forName( "UTF-8" ))));
            acceptor.setHandler(new MinaServerHandler());
            acceptor.getSessionConfig().setReadBufferSize(2048);
            acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
            acceptor.bind(new InetSocketAddress(PORT));
            System.out.println("mina-server-start");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
