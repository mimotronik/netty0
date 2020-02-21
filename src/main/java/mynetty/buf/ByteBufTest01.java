package mynetty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author winterfell
 */
public class ByteBufTest01 {

    public static void main(String[] args) {

        // 1. 创建对象，改对象包含一个数据arr，是一个byte[10]
        // 2. 在netty的buffer中，不需要使用flip进行反转
        //        底层维护了 readerIndex 和 writerIndex
        ByteBuf buffer = Unpooled.buffer(10);

        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }

        System.out.println("capacity = " + buffer.capacity());

        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.getByte(i));
        }

    }
}
