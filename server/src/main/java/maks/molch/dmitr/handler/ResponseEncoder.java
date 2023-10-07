package maks.molch.dmitr.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import maks.molch.dmitr.data.ResponseData;

import java.nio.charset.Charset;

public class ResponseEncoder extends MessageToByteEncoder<ResponseData> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ResponseData msg, ByteBuf out) {
        out.writeBoolean(msg.isSuccess());
        writeStr(out, msg.message());
        out.writeInt(msg.countAttempts());
    }

    private void writeStr(ByteBuf out, String message) {
        out.writeInt(message.length());
        out.writeCharSequence(message, Charset.defaultCharset());
    }
}
