package mynio.channel;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 本地文件写
 * @author winterfell
 **/
public class NIOFileChannel01 {

    public static void main(String[] args) throws Exception {
        String str = "hello world";

        // 1. 创建一个输出流
        FileOutputStream outputStream = new FileOutputStream("D:/tmp/nio/file01.txt");

        // 2. 通过outputStream获取对应的FileChannel
        //    ps: FileChannel 的真实类型是FileChannelImpl
        FileChannel fileChannel = outputStream.getChannel();

        // 3. 创建一个缓冲区 [Debug Point]
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 放入字节
        byteBuffer.put(str.getBytes());

        // 4. 对ByteBuffer 进行flip
        byteBuffer.flip();

        // 5. 将ByteBuffer里面的数据写入到fileChannel  [Debug Point] callback写法
        fileChannel.write(byteBuffer);

        // 6. 关闭流
        outputStream.close();

    }
}
