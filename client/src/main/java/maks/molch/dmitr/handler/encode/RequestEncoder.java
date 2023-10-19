package maks.molch.dmitr.handler.encode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import maks.molch.dmitr.data.request.Request;
import maks.molch.dmitr.interaction.UserInterface;

import java.nio.charset.Charset;

public class RequestEncoder extends MessageToByteEncoder<Request> {
    private final UserInterface userInterface;

    public RequestEncoder(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Request msg, ByteBuf out) {
        userInterface.show("Encoding request...");
        writeStr(out, msg.commandType().name());
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
