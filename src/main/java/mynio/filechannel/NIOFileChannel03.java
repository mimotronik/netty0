package mynio.filechannel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 文件拷贝 使用 ByteBuffer
 *
 * @author winterfell
 **/
public class NIOFileChannel03 {

    public static void main(String[] args) throws Exception {

        // 1. 获取文件输入流
        FileInputStream fileInputStream = new FileInputStream("D:/tmp/nio/file01.txt");

        FileChannel fileChannel01 = fileInputStream.getChannel();

        // 2. 获取文件输出流
        FileOutputStream fileOutputStream = new FileOutputStream("D:/tmp/nio/file01_copy.txt");

        FileChannel fileChannel02 = fileOutputStream.getChannel();

        // 3. 创建Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        while (true) {

            // 6. 这里有个重要的复位操作 标识位重置 ！！！！！
            // 否则 position == limit 新读取的数据写不进来
            byteBuffer.clear();

            // 4.读取数据
            int read = fileChannel01.read(byteBuffer);
            // 读取结束
            if (read == -1) {
                break;
            }
            // 5.将Buffer中的数据写如到fileChannel02
            byteBuffer.flip();
            fileChannel02.write(byteBuffer);
        }

        fileOutputStream.close();
        fileInputStream.close();
    }
}
