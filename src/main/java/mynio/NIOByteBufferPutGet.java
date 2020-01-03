package mynio;

import java.nio.ByteBuffer;

/**
 * 类型化放入取出
 *
 * @author winterfell
 **/
public class NIOByteBufferPutGet {

    public static void main(String[] args) {

        ByteBuffer byteBuffer = ByteBuffer.allocate(64);

        // 类型化放入

        byteBuffer.putInt(100);

        byteBuffer.putLong(123L);

        byteBuffer.putChar('Z');

        byteBuffer.putShort((short)4);

        // 类型化取出
        byteBuffer.flip();

        System.out.println("******************************************");

        System.out.println(byteBuffer.getInt());

        System.out.println(byteBuffer.getLong());

        System.out.println(byteBuffer.getChar());

        System.out.println(byteBuffer.getShort());
    }
}
