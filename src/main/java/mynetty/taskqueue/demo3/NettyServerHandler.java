package mynetty.taskqueue.demo3;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

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

        // 用户自定义定时任务 -> 该任务提交到scheduledTaskQueue中

        ctx.channel().eventLoop().schedule(() -> {
            ctx.writeAndFlush(Unpooled.copiedBuffer("Hello，客户端~ 喵喵4", CharsetUtil.UTF_8));
        }, 5, TimeUnit.SECONDS);

        System.out.println("go on ...");
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

        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello，客户端~ 喵喵1", CharsetUtil.UTF_8));
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
