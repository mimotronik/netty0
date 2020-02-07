package mynio.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author winterfell
 */
public class GroupChatServer {

    // 定义相关的属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    public GroupChatServer() {
        // 初始化的工作
        try {
            // 获取选择器
            selector = Selector.open();

            // 初始化 ServerSocketChannel
            listenChannel = ServerSocketChannel.open();

            // 绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));

            // 设置非阻塞
            listenChannel.configureBlocking(false);

            // 注册到selector上面去
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 处理监听
     */
    public void listen() {
        try {
            while (true) {
                int count = selector.select();
                if (count > 0) { // 有事件处理
                    // 遍历得到的 SelectionKey

                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();

                        // 判断key的事件
                        if (key.isAcceptable()) {  // 处理连接事件
                            SocketChannel socketChannel = listenChannel.accept();

                            socketChannel.configureBlocking(false);

                            // 将该 socketChannel 注册到Selector上
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            // 给出提示
                            System.out.println(socketChannel.getRemoteAddress() + " 上线...");
                        }

                        if (key.isReadable()) { // 通道发生Read事件 即通道是可读的状态
                            // 处理读 （专门写）
                            this.readData(key);
                        }
                        // 防止重复读的事件
                        keyIterator.remove();
                    }

                } else {
                    System.out.println("等待...");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 读取客户端消息
     *
     * @param key
     */
    private void readData(SelectionKey key) {

        // 定义一个SocketChannel
        SocketChannel channel = null;
        try {
            // 取到关联的Channel
            channel = (SocketChannel) key.channel();
            // 创建Buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            int count = channel.read(buffer);

            // 根据count的值作处理
            if (count > 0) {
                // 把缓冲区的数据转成字符串
                String msg = new String(buffer.array());
                // 输出消息
                System.out.println("from 客户端: " + msg);

                // 向其他客户端转发消息 （专门写一个方法处理）
                this.sendInfoToOtherClients(msg, channel);
            }
        } catch (IOException e) {
            // e.printStackTrace();
            try {
                System.out.println(channel.getRemoteAddress() + " 离线...");
                // 离线的处理

                // 取消注册
                key.cancel();
                // 关闭通道
                channel.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 转发消息给其他客户端（通道）
     *
     * @param msg
     * @param self
     */
    private void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException {

        System.out.println("服务器转发消息");

        // 遍历所有注册到selector上的 SocketChannel 并排除self
        for (SelectionKey key : selector.keys()) {
            Channel targetChannel = key.channel();
            // 排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                SocketChannel targetSocketChannel = (SocketChannel) targetChannel;
                // 将msg 存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                targetSocketChannel.write(buffer);
            }
        }
    }


    public static void main(String[] args) {

        GroupChatServer groupChatServer = new GroupChatServer();

        groupChatServer.listen();

    }
}
