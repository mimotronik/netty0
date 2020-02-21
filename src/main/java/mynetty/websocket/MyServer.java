package mynetty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author winterfell
 */
public class MyServer {

    public static void main(String[] args) throws Exception {

        EventLoopGroup boss = new NioEventLoopGroup();

        EventLoopGroup worker = new NioEventLoopGroup();

        try {

            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            ChannelPipeline pipeline = ch.pipeline();

                            // 基于http的编码器和解码器
                            pipeline.addLast(new HttpServerCodec());
                            // 是以块的方式写的
                            pipeline.addLast(new ChunkedWriteHandler());
                            /*
                             * 1 说明 http的数据在传输过程中是分段的，HttpObjectAggregator就是可以将多个段聚合起来
                             * 2 这就是为什么当浏览器发送大量数据时，浏览器会发送多次http请求
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));

                            /*
                             * 1 对于websocket 它的数据是以帧(frame)的方式传输的
                             * 2 可以看到 WebsocketFrame 下面有6个子类
                             * 3 浏览器发送请求时 ws://localhost:7000/hello [/hello 表示请求的uri]
                             * 4 WebSocketServerProtocolHandler核心功能是将http协议升级为ws协议 保持长连接
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                            // 自定义 handler 处理业务逻辑
                            pipeline.addLast(new MyServerHandler());
                        }
                    });

            System.out.println("server is ready ...");

            ChannelFuture channelFuture = bootstrap.bind(7000).sync();

            channelFuture.channel().closeFuture().sync();

        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
