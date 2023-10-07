package maks.molch.dmitr.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import maks.molch.dmitr.data.ResponseData;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.concurrent.Semaphore;

public class ClientInboundHandler extends ChannelInboundHandlerAdapter {
    private final List<ResponseData> responses;
    private final Semaphore responseSemaphore;

    public ClientInboundHandler(List<ResponseData> responses, Semaphore responseSemaphore) {
        this.responses = responses;
        this.responseSemaphore = responseSemaphore;
    }

    @Override
    public void channelRead(@NonNull ChannelHandlerContext ctx, @NonNull Object msg) {
        ResponseData responseData = (ResponseData) msg;
        responses.add(responseData);
        responseSemaphore.release();
    }
}
