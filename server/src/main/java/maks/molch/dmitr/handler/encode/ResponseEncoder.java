package maks.molch.dmitr.handler.encode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import maks.molch.dmitr.data.response.Response;

import java.nio.charset.Charset;

public class ResponseEncoder extends MessageToByteEncoder<Response> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Response msg, ByteBuf out) {
        writeStr(out, msg.status().name());
        out.writeInt(msg.arguments().length);
        for (var arg : msg.arguments()) {
            writeStr(out, arg);
        }
        out.writeInt(msg.payload().length);
        out.writeBytes(msg.payload());
    }

    private void writeStr(ByteBuf out, String string) {
        out.writeInt(string.length());
        out.writeCharSequence(string, Charset.defaultCharset());
    }
}
