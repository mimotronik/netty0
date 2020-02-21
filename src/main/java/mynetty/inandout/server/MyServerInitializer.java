package mynetty.inandout.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import mynetty.inandout.decoderandendocer.MyByteToLongDecoder2;
import mynetty.inandout.decoderandendocer.MyLongToByteEncoder;

/**
 * @author winterfell
 */
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 入站handler 进行解码
//        pipeline.addLast(new MyByteToLongDecoder());
        pipeline.addLast(new MyByteToLongDecoder2());

        pipeline.addLast(new MyLongToByteEncoder());
        // 自定义的handler处理业务逻辑
        pipeline.addLast(new MyServerHandler());

    }
}
