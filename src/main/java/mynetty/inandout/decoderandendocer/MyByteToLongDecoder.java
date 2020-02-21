package mynetty.inandout.decoderandendocer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author winterfell
 */
public class MyByteToLongDecoder extends ByteToMessageDecoder {

    /**
     * @param ctx 上下文对象
     * @param in  入栈的ByteBuf
     * @param out list集合 将解码后的数据传给下一个handler处理
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        System.out.println("MyByteToLongDecoder decode run ...");

        // 因为long是8个字节

        // 只有8个字节才能读取一个 long
        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }
}
