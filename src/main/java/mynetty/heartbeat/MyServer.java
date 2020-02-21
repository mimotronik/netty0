package mynetty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

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
                    .handler(new LoggingHandler(LogLevel.INFO))  // 在bossGroup里面增加一个日志处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            // 增加netty提供的处理器 IdleStateHandler

                            /*
                             * 1. IdleStateHandler 是 netty提供的处理空闲状态的处理器
                             * 2. long readerIdleTime：表示多长时间没有读（server没有读），就会发送一个心跳检测包 检测 是否连接
                             * 3. long writerIdleTime：表示多长时间没有写（server没有写），就会发送一个心跳检测包 检测 是否连接
                             * 4. long allIdleTime： 表示多长时间既没读也没写，就会发送一个心跳检测包 检测 是否连接
                             */
                            /* 5. 文档说明 看下源码
                            public IdleStateHandler(long readerIdleTime, long writerIdleTime, long allIdleTime,TimeUnit unit)
                            }
                            */
                            /*
                             * 6.当 IdleStateHandler触发后，就会传递给管道 的下一个handler去处理
                             * 通过调用（或触发）下一个handler 的userEventTrigger 在改方法中处理IdleStateent
                             * IdleStateHandler触发 下一个handler 的userEventTrigger 处理
                             */
                            pipeline.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));

                            // 加入一个对空闲监测自定义的处理器
                            pipeline.addLast(new MyServerHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();

        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

}
