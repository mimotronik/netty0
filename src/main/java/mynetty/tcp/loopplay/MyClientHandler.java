package mynetty.tcp.loopplay;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;

/**
 * @author winterfell
 */
public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        String msg = "client msg " + UUID.randomUUID().toString();

        byte[] content = msg.getBytes(Charset.forName("UTF-8"));
        int len = content.length;

        // 创建协议包对象
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(len);
        messageProtocol.setContent(content);
        ctx.writeAndFlush(messageProtocol);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {

        int len = msg.getLen();
        byte[] content = msg.getContent();

        System.out.println("##############################################################");
        System.out.println("客户端接收的消息如下");
        System.out.println("长度：" + len);
        System.out.println("内容：" + new String(content, Charset.forName("utf-8")));
        System.out.println("接收消息数量：" + ++count);


        // 回复给服务端

        String responseContentStr = "client msg " + UUID.randomUUID().toString();

        byte[] responseContent = responseContentStr.getBytes(Charset.forName("UTF-8"));
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
