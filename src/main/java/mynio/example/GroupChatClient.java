package mynio.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author winterfell
 */
public class GroupChatClient {

    private final String HOST = "127.0.0.1";
    private final int PORT = 6667;

    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    public GroupChatClient() throws IOException {
        // 初始化
        this.selector = Selector.open();

        this.socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));

        socketChannel.configureBlocking(false);

        socketChannel.register(selector, SelectionKey.OP_READ);

        this.username = socketChannel.getLocalAddress().toString().substring(1);

        System.out.println(username + " is ok ...");
    }


    /**
     * 向服务器发送消息
     *
     * @param msg
     */
    public void sendInfo(String msg) {
        msg = username + " 说：" + msg;
        try {
            ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
            socketChannel.write(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从服务器读取消息
     */
    public void readInfo() {
        try {
            int readChannels = selector.select();
            if (readChannels > 0) { // 有可用的通道
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isReadable()) {
                        // 得到相关的通道
                        SocketChannel sc = (SocketChannel) key.channel();
                        // 得到一个buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        sc.read(buffer);
                        String msg = new String(buffer.array());
                        System.out.println(msg.trim());
                    }

                    keyIterator.remove();
                }
            } else {
//                System.out.println("没有可用的通道");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        GroupChatClient groupChatClient = new GroupChatClient();

        new Thread() {
            public void run() {
                while (true) {
                    groupChatClient.readInfo();
                    try {
                        Thread.currentThread().sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();


        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            groupChatClient.sendInfo(line);
        }

    }
}
