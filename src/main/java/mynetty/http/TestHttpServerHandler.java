package mynetty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * SimpleChannelInboundHandler extends ChannelInboundHandlerAdapter
 * <p>
 * HttpObject 表示成客户端和服务端相互通讯的数据为HttpObject
 *
 * @author winterfell
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        // 判断msg是不是HttpRequest请求
        if (msg instanceof HttpRequest) {

            // 用来验证不同的请求会使用不同的Pipeline和handler

            System.out.println("Pipeline的hashCode : " + ctx.channel().pipeline().hashCode());
            System.out.println("ChannelHandler的hashCode : " + this.hashCode());

            System.out.println("msg 类型" + msg.getClass());
            System.out.println("客户端地址:" + ctx.channel().remoteAddress());

            // 回复信息给浏览器[Http协议]

            ByteBuf content = Unpooled.copiedBuffer("hello,我是服务器", CharsetUtil.UTF_8);

            HttpRequest httpRequest = (HttpRequest) msg;
            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求了 favicon.ico 不作响应");
                return;
            }

            // 构造一个http响应
            DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            ctx.writeAndFlush(httpResponse);
        }
    }
}
