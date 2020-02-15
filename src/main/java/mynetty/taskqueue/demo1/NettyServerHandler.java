package mynetty.taskqueue.demo1;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
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

        // 比如这里有一个 耗时长 的任务 -> 异步执行 -> 提交该channel 对应的
        // NioEventLoop中的taskQueue

        // 解决方案1： 用户程序自定义的普通任务
        EventLoop eventExecutors = ctx.channel().eventLoop();

        // 提交到EventLoop 里面的taskQueue
        eventExecutors.execute(() -> {
            try {
                Thread.sleep(10 * 1000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("Hello，客户端~ 喵喵2", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });


        eventExecutors.execute(() -> {
            try {

                // Hello，客户端~ 喵喵3 是30秒过后才会在客户端显示，因为 队列是在同一个线程里面里的

                Thread.sleep(20 * 1000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("Hello，客户端~ 喵喵3", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });

//        Thread.sleep(10 * 1000);
//        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello，客户端~ 喵喵2", CharsetUtil.UTF_8));

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
