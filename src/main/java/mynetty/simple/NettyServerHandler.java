package mynetty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
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

        System.out.println("server ctx " + ctx);

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
        ctx.close();
    }
}
