package mynio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 本地文件读
 *
 * @author winterfell
 **/
public class NIOFileChannel02 {

    public static void main(String[] args) throws Exception {

        // 1. 创建文件的输入流
        File file = new File("D:/tmp/nio/file01.txt");
        FileInputStream inputStream = new FileInputStream(file);

        // 2. 获取 FileChannel --> 实际类型 FileChannelImpl
        FileChannel fileChannel = inputStream.getChannel();

        // 3. 创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        // 4. 将通道的数据读入到buffer中
        fileChannel.read(byteBuffer);

        System.out.println(new String(byteBuffer.array()));

        inputStream.close();

    }
}
