package mynio.selector;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author winterfell
 */
public class NIOServer {

    public static void main(String[] args) throws Exception {

        // 创建ServerSocketChannel -> ServerSocket

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到一个 selector 对象
        Selector selector = Selector.open();

        // 绑定一个端口 6666 在服务端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 把 serverSocketChannel 注册到 selector 关心事件为

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端连接
        while (true) {
            // 这里等待 1 秒 如果没有 事件发生 返回
            if (selector.select(1000) == 0) {
                // 没有任何事件发生
                System.out.println("nothing happened ...");
                continue;
            }

            // 如果返回的大于0 获取到相关的 SelectionKey 的集合
            // 1. 如果返回的 >0 表示已经过去到关注的事件
            // 2. 通过selectionKey反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()) {
                // 获取到selectionKey
                SelectionKey selectionKey = keyIterator.next();
                // 根据key对应的通道发生的事件作相应的处理

                if (selectionKey.isAcceptable()) { // 如果是 OP_ACCEPT 有新的客户端连接
                    // 给该客户端生成一个SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();

                    System.out.println("客户端连接成功，生成一个socketChannel " + socketChannel.hashCode());

                    // 将当前的 SocketChannel 注册到Selector上,关注事件为 OP_READ
                    // 关联一个Buffer

                    /*
                     * 将socketChannel设置为非阻塞的
                     *
                     * java.nio.channels.IllegalBlockingModeException
                     */
                    socketChannel.configureBlocking(false);

                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                if (selectionKey.isReadable()) { // 发生 OP_READ
                    // 通过key 方向获取对应的channel
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    // 获取到该channel获取的Buffer
                    ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                    channel.read(byteBuffer);
                    System.out.println("from 客户端 " + new String(byteBuffer.array()));
                }

                // 手动从集合中移除当前的SelectionKey 防止重复操作
                keyIterator.remove();
            }

        }

    }
}
