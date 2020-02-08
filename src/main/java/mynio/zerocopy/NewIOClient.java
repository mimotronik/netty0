package mynio.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @author winterfell
 */
public class NewIOClient {
    public static void main(String[] args) throws IOException {

        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.socket().connect(new InetSocketAddress("127.0.0.1", 7001));

        String fileName = "D:/Shadowsocks-4.1.8.0.zip";

        FileChannel fileChannel = new FileInputStream(fileName).getChannel();

        long startTime = System.currentTimeMillis();

        // 在linux下一个transferTo 方法就可以完成传输
        // 在windows下 一次调用transferTo 只能发送8M , 就需要分段传输文件 而且要注意传输时的位置

        // 传输时的位置需要注意以下

        // transferTo 底层使用零拷贝
        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);

        System.out.println("发送的总的字节数为：" + transferCount + " 耗时：" + (System.currentTimeMillis() - startTime));

        // 关闭
        fileChannel.close();
    }
}
