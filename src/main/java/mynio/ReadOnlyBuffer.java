package mynio;

import java.nio.ByteBuffer;

/**
 * @author winterfell
 **/
public class ReadOnlyBuffer {

    public static void main(String[] args) {

        // 创建一个Buffer
        ByteBuffer buffer = ByteBuffer.allocate(64);

        for (int i = 0; i < 64; i++) {
            buffer.put((byte) i);
        }

        // 翻转
        buffer.flip();

        // 得到一个只读的Buffer

        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();

        System.out.println(readOnlyBuffer.getClass());

        while(readOnlyBuffer.hasRemaining()){
            System.out.println(readOnlyBuffer.get());
        }

        // java.nio.ReadOnlyBufferException
        readOnlyBuffer.put((byte) 100);
    }
}
