package mynetty.tcp.protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;

/**
 * @author winterfell
 */
public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        // 接收到数据并处理

        int len = msg.getLen();

        byte[] content = msg.getContent();

        System.out.println("--------------------------------------------------------------");

        System.out.println("服务器接收的消息如下 ");
        System.out.println("长度：" + len);
        System.out.println("内容：" + new String(content, Charset.forName("utf-8")));
        System.out.println("接收的消息数量:" + ++count);

        System.out.println("##############################################################");

        String responseContentStr = UUID.randomUUID().toString();
        byte[] responseContent = responseContentStr.getBytes(Charset.forName("utf-8"));

        int responseLen = responseContent.length;

        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(responseLen);
        messageProtocol.setContent(responseContent);

        ctx.writeAndFlush(messageProtocol);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
