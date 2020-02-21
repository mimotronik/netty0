package mynetty.tcp.protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

/**
 * @author winterfell
 */
public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

//        for (int i = 0; i < 10; i++) {
//            // 使用客户端发送10条数据 hello,server
//            ctx.writeAndFlush(Unpooled.copiedBuffer("hello,server" + i, CharsetUtil.UTF_8));
//        }

        // 发送10条数据
        for (int i = 0; i < 10; i++) {
            String msg = "今天天气好。。";

            byte[] content = msg.getBytes(Charset.forName("UTF-8"));
            int len = content.length;

            // 创建协议包对象
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(len);
            messageProtocol.setContent(content);
            ctx.writeAndFlush(messageProtocol);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {

        int len = msg.getLen();
        byte[] content = msg.getContent();

        System.out.println("--------------------------------------------------------------");
        System.out.println("客户端接收的消息如下");
        System.out.println("长度：" + len);
        System.out.println("内容：" + new String(content, Charset.forName("utf-8")));
        System.out.println("接收消息数量：" + ++count);
        System.out.println("##############################################################");

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
