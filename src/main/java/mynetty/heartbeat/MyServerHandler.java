package mynetty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author winterfell
 */
public class MyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * evt 事件
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            // evt 向下转型
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

            String eventType = "";

            switch (idleStateEvent.state()) {

                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress() + "--超时事件发生--" + eventType);
            System.out.println("服务器做相应处理");

            // 如果发生空闲直接关闭通道 空闲事件只会出现一次
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
