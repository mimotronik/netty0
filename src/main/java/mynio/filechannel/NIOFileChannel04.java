package mynio.filechannel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * 文件拷贝使用 transferFrom
 *
 * @author winterfell
 **/
public class NIOFileChannel04 {

    public static void main(String[] args) throws Exception {

        FileInputStream fileInputStream = new FileInputStream("D:/tmp/nio/apache-tomcat-7.0.86.zip");
        FileOutputStream fileOutputStream = new FileOutputStream("D:/tmp/nio/apache-tomcat-7.0.86_copy.zip");

        FileChannel source = fileInputStream.getChannel();
        FileChannel dest = fileOutputStream.getChannel();

        // 使用transferFrom完成拷贝
        dest.transferFrom(source, 0, source.size());

        // 关闭相关的通道和流
        source.close();
        dest.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
