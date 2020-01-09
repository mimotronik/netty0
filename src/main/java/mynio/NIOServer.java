package mynio;

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
 **/
public class NIOServer {

    public static void main(String[] args) throws Exception {

        // 1. 创建SeverSocketChannel -> ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 2. 得到一个Selector对象
        Selector selector = Selector.open();

        // 3.绑定一个端口在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        // 4.设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 5. ServerSocketChannel 也要注册到 Selector 上面去 关心事件为 OP_ACCESS

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 6. 循环等待
        while (true) {

            // 这里等待一秒 如果1秒钟没有事件发生，返回继续等待
            if (selector.select(1000) == 0) {
                System.out.println("服务器等待了1秒，无连接");
                continue;
            }

            // 如果返回的的 >0  获取到相关的SelectionKey集合
            // 1. 如果返回的的 >0  表示已经获取到关注的事件了
            // 2. selector.selectedKeys(); 关注事件的集合
            //   通过 SelectionKey 反向获取 Channel
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()) {
                // 获取到SelectionKey
                SelectionKey key = keyIterator.next();

                // 根据key 对应的通道发生的事件做相应的处理
                if (key.isAcceptable()) { // 如果是 OP_ACCESS 有新的客户端连接
                    // 给该客户端生成 SocketChannel 这个 accept() 不阻塞 事件驱动的
                    SocketChannel socketChannel = serverSocketChannel.accept();

                    // 将当前的 SocketChannel 注册到 Selector上 关注事件为OP_READ 同事给SocketChannel 关联一个Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                if (key.isReadable()) { // 发生了 OP_READ

                    // 通过key 反向获取到对应的 Channel
                    SocketChannel channel = (SocketChannel) key.channel();

                    // 获取到该Channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();

                    channel.read(buffer);

                    System.out.println("from 客户端 " + new String(buffer.array()));
                }

                // 手动从集合中移除当前的selectionKey,防止重复操作
                keyIterator.remove();
            }
        }

    }
}
