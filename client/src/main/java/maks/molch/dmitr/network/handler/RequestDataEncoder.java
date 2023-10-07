package maks.molch.dmitr.network.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import maks.molch.dmitr.data.RequestData;

import java.nio.charset.Charset;

public class RequestDataEncoder extends MessageToByteEncoder<RequestData> {
    @Override
    protected void encode(ChannelHandlerContext ctx, RequestData msg, ByteBuf out) {
        writeStr(msg.username(), out);
        writeStr(msg.password(), out);
    }

    private void writeStr(String str, ByteBuf out) {
        out.writeInt(str.length());
        out.writeCharSequence(str, Charset.defaultCharset());
    }
}
