package mynetty.inandout.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author winterfell
 */
public class MyClientHandler extends SimpleChannelInboundHandler<Long> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {

        System.out.println("服务器地址 " + ctx.channel().remoteAddress());
        System.out.println("服务器收到的消息: " + msg);
    }

    /**
     * 发送数据
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("MyClientHandler 发送数据");

        // 直接发送一个Long
        ctx.writeAndFlush(123456L);
    }
}
