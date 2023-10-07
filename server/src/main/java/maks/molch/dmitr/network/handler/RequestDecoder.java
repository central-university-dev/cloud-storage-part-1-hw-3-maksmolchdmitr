package maks.molch.dmitr.network.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import maks.molch.dmitr.data.RequestData;

import java.nio.charset.Charset;
import java.util.List;

public class RequestDecoder extends ReplayingDecoder<RequestDecoder> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        String username = readStr(in);
        String password = readStr(in);
        out.add(new RequestData(username, password));
    }

    private String readStr(ByteBuf in) {
        int length = in.readInt();
        return in.readCharSequence(length, Charset.defaultCharset()).toString();
    }
}
