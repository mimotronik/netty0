package mynetty.codec.protobuf2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author winterfell
 */
public class NettyServerHandler2 extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {

        // 根据dataType显示不同信息
        MyDataInfo.MyMessage.DataType dataType = msg.getDataType();

        if (dataType == MyDataInfo.MyMessage.DataType.StudentType) {

            System.out.println("student id = " + msg.getStudent().getId() + " name = " + msg.getStudent().getName());

        } else if (dataType == MyDataInfo.MyMessage.DataType.WorkerType) {

            System.out.println("worker id = " + msg.getWorker().getId() + " name = " + msg.getWorker().getName());

        } else {
            System.out.println("传输的类型不正确");
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
