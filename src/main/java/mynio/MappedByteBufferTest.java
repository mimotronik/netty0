package mynio;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 说明：
 * 1. MappedByteBuffer 可以让文件直接在内存（堆外内存）中修改，操作系统不需要拷贝一次
 *
 * @author winterfell
 **/
public class MappedByteBufferTest {

    public static void main(String[] args) throws Exception {

        RandomAccessFile randomAccessFile = new RandomAccessFile("D:/tmp/nio/file01.txt", "rw");

        // 获取对应的通道
        FileChannel channel = randomAccessFile.getChannel();

        /*
         *  参数1： 表明使用的是读写模式
         *  参数2： 可以直接修改的起始位置
         *  参数3： 映射到内存的大小，即可以文件 多少个字节映射到内存
         *  可以直接修改的范围就是 [0,5)
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(1, (byte) 'X');
        mappedByteBuffer.put(3, (byte) 'Y');

        randomAccessFile.close();

        System.out.println("文件修改成功");

    }
}
