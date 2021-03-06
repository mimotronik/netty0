package mynetty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

/**
 * 自定义的handler要继承netty里面规定好的某个Handler
 *
 * @author winterfell
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    /**
     * 读取数据的事件 (读取客户端发送的消息)
     * 1. ChannelHandlerContext:上下文对象，含有pipeline，通道，地址等
     * 2. msg 客户端发送的数据 默认是Object的形式
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // 输出服务器读取线程

        System.out.println("服务器读取线程:" + Thread.currentThread().getName());

        System.out.println("server ctx " + ctx);

        Channel channel = ctx.channel();
        ChannelPipeline pipeline = ctx.pipeline(); // 本质是一个双向列表 涉及到入站出战问题

        System.out.println("channel 和 pipeline 的关系 -->" +
                " channel.pipeline().equals(pipeline) :" + channel.pipeline().equals(pipeline) + "。 pipeline.channel().equals(channel):" + pipeline.channel().equals(channel));

        // 对msg进行处理

        // ByteBuf 由 netty 提供
        ByteBuf buf = (ByteBuf) msg;

        System.out.println("客户端发送消息为:" + buf.toString(CharsetUtil.UTF_8));

        System.out.println("客户端地址:" + ctx.channel().remoteAddress());
    }


    /**
     * 数据读取完毕
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        // 将数据写入缓冲并刷新
        // 一般来将 对 发送的数据 进行编码

        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello，客户端~", CharsetUtil.UTF_8));
    }

    /**
     * 处理异常 一般是关闭通道
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
