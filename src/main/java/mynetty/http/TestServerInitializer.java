package mynetty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author winterfell
 */
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 管道添加处理器

        // HttpServerCodec 作用说明
        // Netty提供的http的编解码器
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
        // 增加一个自定义的处理器
        pipeline.addLast("MyTestHttpServerHandler", new TestHttpServerHandler());
    }
}
