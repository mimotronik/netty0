package mynio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering：将数据写入到buffer时，可以采用buffer数组，依次写入
 * Gathering： 从buffer读取数据时，可以采用buffer，依次读写
 *
 * @author winterfell
 **/
public class ScatteringAndGatheringTest {

    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        // 绑定端口到serverSocket并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        // 创建一个Buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        // 等待客户端的连接(telnet)
        SocketChannel socketChannel = serverSocketChannel.accept();
        int messageLength = 8;
        // 循环读取
        while (true) {

            int byteRead = 0;

            while (byteRead < messageLength) {
                long read = socketChannel.read(byteBuffers);
                byteRead += read; // 累积读取的字节数
                System.out.println("byteRead=" + byteRead);

                // 使用流打印
                Arrays.asList(byteBuffers).stream().map(buffer -> "position=" + buffer.position() + ",limit=" + buffer.limit()).forEach(System.out::println);
            }

            // 将所有的buffer进行flip
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());

            // 将数据读出显示到客户端
            long byteWrite = 0;
            while (byteWrite < messageLength) {
                long l = socketChannel.write(byteBuffers);
                byteWrite += l;
            }

            // 将所有的buffer clean
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.clear());

            System.out.println("byteRead:=" + byteRead + " byteWrite=" + byteWrite + ", messagelength" + messageLength);

        }

    }
}
