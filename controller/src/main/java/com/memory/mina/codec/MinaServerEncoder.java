package com.memory.mina.codec;

import com.memory.mina.MinaSessionCache;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.nio.charset.Charset;

/**
 * @program socketjar
 * @Author: cui.Memory
 * @Date: 2018/12/11 18:29
 * @description:
 */
public class MinaServerEncoder implements ProtocolEncoder {
    public void dispose(IoSession ioSession) throws Exception {

    }

    public void encode(IoSession ioSession, Object message, ProtocolEncoderOutput protocolEncoderOutput) throws Exception {
        String msg = null;
        if(message instanceof String){
            msg = (String) message;
        }
        if(msg != null){
            IoBuffer ioBuffer = IoBuffer.allocate(msg.length());
            ioBuffer.setAutoExpand(true);
            ioBuffer.putString(msg, Charset.defaultCharset().newEncoder());
            ioBuffer.flip();
            protocolEncoderOutput.write(ioBuffer);
        }else{
            System.out.println("传输数据格式异常");
        }
    }
}
