package mynetty.codec.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author winterfell
 */
public class NettyServerHandler2 extends SimpleChannelInboundHandler<StudentPOJO.Student> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, StudentPOJO.Student student) throws Exception {
        System.out.println("客户端发送的数据 id=" + student.getId() + " name=" + student.getName());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
