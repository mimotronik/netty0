package mynetty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * 范型 TextWebSocketFrame 表示是一个文本帧
 *
 * @author winterfell
 */
public class MyServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

        System.out.println("服务器端收到消息:" + msg.text());

        // 回复客户端

        ctx.channel().writeAndFlush(
                new TextWebSocketFrame(
                        "服务器时间" + LocalDateTime.now() + " " + msg.text()
                )
        );
    }

    /**
     * 当 web客户端连接后就会触发这个方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

        // asLongText 是唯一的
        System.out.println("handlerAdded被调用 " + ctx.channel().id().asLongText());

        System.out.println("handlerAdded被调用 " + ctx.channel().id().asShortText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

        // asLongText 是唯一的
        System.out.println("handlerRemoved被调用 " + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
