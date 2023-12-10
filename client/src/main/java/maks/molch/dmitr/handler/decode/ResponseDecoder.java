package maks.molch.dmitr.handler.decode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import maks.molch.dmitr.data.response.Response;
import maks.molch.dmitr.data.response.ResponseStatus;
import maks.molch.dmitr.interaction.UserInterface;

import java.nio.charset.Charset;
import java.util.List;

public class ResponseDecoder extends ReplayingDecoder<Response> {
    private final UserInterface userInterface;

    public ResponseDecoder(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        userInterface.show("Decoding response...");
        ResponseStatus responseStatus = ResponseStatus.valueOf(readStr(in));
        int argumentCount = in.readInt();
        String[] arguments = new String[argumentCount];
        for (int i = 0; i < argumentCount; i++) {
            arguments[i] = readStr(in);
        }
        int bytesCount = in.readInt();
        byte[] payload = new byte[bytesCount];
        in.readBytes(payload);
        out.add(new Response(responseStatus, arguments, payload));
    }

    private String readStr(ByteBuf in) {
        int length = in.readInt();
        return in.readCharSequence(length, Charset.defaultCharset()).toString();
    }
}
