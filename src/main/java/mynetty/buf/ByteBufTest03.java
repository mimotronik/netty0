package mynetty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;


/**
 * @author winterfell
 */
public class ByteBufTest03 {

    public static void main(String[] args) {

        ByteBuf buffer = Unpooled.copiedBuffer("Hello,World!", CharsetUtil.UTF_8);

        if (buffer.hasArray()) {

            byte[] content = buffer.array();

            System.out.println(new String(content, Charset.forName("utf-8")));

            System.out.println("ByteBuf: " + buffer);

            System.out.println("arrayOffset = " + buffer.arrayOffset());

            System.out.println("readerIndex = " + buffer.readerIndex());

            System.out.println("writerIndex = " + buffer.writerIndex());

            System.out.println("capacity = " + buffer.capacity());


            buffer.readByte();
            int len = buffer.readableBytes();
            System.out.println("len = " + len);

            // 按照区间读取
            System.out.println(buffer.getCharSequence(0, 4, Charset.forName("utf-8")));

            System.out.println(buffer.getCharSequence(4, 11, Charset.forName("utf-8")));

        }

    }

}
