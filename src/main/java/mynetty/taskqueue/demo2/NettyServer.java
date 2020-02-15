package mynetty.taskqueue.demo2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author winterfell
 */
public class NettyServer {
    public static void main(String[] args) throws Exception {

        /*
         * 1.创建两个线程组 bossGroup和workerGroup
         * 2.boosGroup和只是处理连接请求，workerGroup 客户端真正的业务请求
         * 3.两个都是无线循环
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务端的启动对象
            ServerBootstrap bootstrap = new ServerBootstrap();

            // 使用链式编程 配置参数
            bootstrap.group(bossGroup, workerGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class) // 使用 NioServerSocketChannel 作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列等待连接的个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 匿名创建一个通道初始化对象

                        // 给 pipeline 设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new NettyServerHandler());
                        }
                    }); // 给workerGroup的管道 设置处理器

            System.out.println("server is ready ...");

            // 绑定一个端口并且同步，生产一个ChannelFuture对象
            // 启动服务器并绑定端口
            ChannelFuture channelFuture = bootstrap.bind(6668).sync();

            // 对 关闭通道 进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
