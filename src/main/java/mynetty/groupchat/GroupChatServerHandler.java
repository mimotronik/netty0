package mynetty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author winterfell
 */
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    // 定义一个Channel组管理所有的channel
    // GlobalEventExecutor.INSTANCE 是一个全局的事件执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static Map<String, Channel> channels = new HashMap<>();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:dd");


    /**
     * handlerAdded 表示连接建立，一旦连接，第一个被执行
     * 将当前的channel 加入到 channelGroup
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel currentChannel = ctx.channel();
        // 将该客户加入聊天的信息推送给其他在线的客户端

        /*
         * 该方法会将ChannelGroup中所有的channel遍历并发送消息
         */
        channelGroup.writeAndFlush("[客户端]" + sdf.format(new Date()) + " " + currentChannel.remoteAddress() + " 加入聊天 \n");
        channelGroup.add(currentChannel);
    }

    /**
     * handlerRemoved 表示断开连接
     * 将当前的channel 加入到 channelGroup
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 将 xxx离线 推动给当前在线的
        Channel channel = ctx.channel();
        // handlerRemoved 一但触发 handlerRemoved,channelGroup会自动剔除当前channel
        channelGroup.writeAndFlush("[客户端]" + sdf.format(new Date()) + " " + channel.remoteAddress() + " 离开了 \n");
        System.out.println("当前channelGroup size:" + channelGroup.size());
    }

    /**
     * 表示 channel 处于一个活动的状态
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 上线了");
    }

    /**
     * 表示 channel 处于失活的状态
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 下线了");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 读取并转发
        Channel currentChannel = ctx.channel();

        // 遍历channelGroup 根据不同情况回送不同消息

        channelGroup.forEach(channel -> {
            if (!channel.equals(currentChannel)) {
                // 不是当前channel 直接转发
                channel.writeAndFlush("[客户]" + sdf.format(new Date()) + " " + currentChannel.remoteAddress() + " 发送消息:" + msg + "\n");
            } else {
                channel.writeAndFlush("[自己]" + sdf.format(new Date()) + " " + " 发送消息:" + msg + "\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
