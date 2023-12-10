package maks.molch.dmitr.handler.response;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import maks.molch.dmitr.data.response.Response;
import maks.molch.dmitr.interaction.UserInterface;

public class ResponseHandlerProcessing extends ChannelInboundHandlerAdapter {
    private final UserInterface userInterface;

    public ResponseHandlerProcessing(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Response response = (Response) msg;
        userInterface.show(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
