package mynetty.taskqueue.demo1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author winterfell
 */
public class NettyClient {

    public static void main(String[] args) throws Exception {

        EventLoopGroup eventExecutors = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new NettyClientHandler());
                        }
                    });

            System.out.println("client is ok ...");

            // 启动客户端去连接服务端

            // channelFuture 涉及到netty的异步模型
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6668).sync();

            // 给关闭的通道监听
            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            eventExecutors.shutdownGracefully();
        }
    }

}
