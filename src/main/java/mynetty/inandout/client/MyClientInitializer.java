package mynetty.inandout.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import mynetty.inandout.decoderandendocer.MyByteToLongDecoder2;
import mynetty.inandout.decoderandendocer.MyLongToByteEncoder;

/**
 * @author winterfell
 */
public class MyClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();

        // 加入出站的handler 对数据进行编码  Long类型数据变成 Byte
        pipeline.addLast(new MyLongToByteEncoder());

//        pipeline.addLast(new MyByteToLongDecoder());
        pipeline.addLast(new MyByteToLongDecoder2());

        // 再加入一个自定义的handler处理业务逻辑
        pipeline.addLast(new MyClientHandler());
    }
}
