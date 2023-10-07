package maks.molch.dmitr.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import maks.molch.dmitr.data.ResponseData;

import java.nio.charset.Charset;
import java.util.List;

public class ResponseDataDecoder extends ReplayingDecoder<ResponseData> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        out.add(new ResponseData(in.readBoolean(), readStr(in), in.readInt()));
    }

    private String readStr(ByteBuf in) {
        int length = in.readInt();
        return in.readCharSequence(length, Charset.defaultCharset()).toString();
    }
}
