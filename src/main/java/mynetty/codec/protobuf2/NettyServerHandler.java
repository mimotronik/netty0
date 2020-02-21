package mynetty.codec.protobuf2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import mynetty.codec.protobuf.StudentPOJO;

/**
 * 自定义的handler要继承netty里面规定好的某个Handler
 *
 * @author winterfell
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // 读取客户端发送的内容
        StudentPOJO.Student student = (StudentPOJO.Student) msg;

        System.out.println("客户端发送的数据 id=" + student.getId() + " name=" + student.getName());

    }


    /**
     * 数据读取完毕
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
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
