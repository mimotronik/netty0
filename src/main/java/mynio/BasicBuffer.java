package mynio;

import java.nio.IntBuffer;

/**
 * @author winterfell
 **/
public class BasicBuffer {

    public static void main(String[] args) {

        // 举例说明buffer的使用

        // 1. 创建buffer
        //   创建一个Buffer 大小为5 即可以存放 5 个int
        IntBuffer intBuffer = IntBuffer.allocate(5);

        // 2. 向buffer中存放数据
//        intBuffer.put(10);
//        intBuffer.put(11);
//        intBuffer.put(12);
//        intBuffer.put(13);
//        intBuffer.put(14);
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i * 2);
        }

        // 如何取出buffer里面的数据

        // 3. Buffer的读写切换
/*
        public final Buffer flip() {
            limit = position;  // 反转之后读取的数据不能超过position 因为在put数据的时候 position 会改变
            position = 0;  // 从第一个开始读
            mark = -1;
            return this;
        }
*/
        intBuffer.flip();

        // 从 position位置为1的地方读取
        intBuffer.position(1);
        // 不能超过 <3 所以只能读到 2
        intBuffer.limit(3);

        // 4. 取出Buffer里面的值
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }

    }
}
