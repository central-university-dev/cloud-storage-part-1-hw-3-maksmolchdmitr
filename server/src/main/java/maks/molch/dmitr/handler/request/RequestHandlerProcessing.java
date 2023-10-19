package maks.molch.dmitr.handler.request;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import maks.molch.dmitr.data.request.Request;
import maks.molch.dmitr.data.response.Response;

import static maks.molch.dmitr.data.request.CommandType.AUTHENTICATION;
import static maks.molch.dmitr.data.response.ResponseStatus.ACCESS_DENIED;

public class RequestHandlerProcessing extends ChannelInboundHandlerAdapter {
    private final RequestHandlerResponsibilityChain requestHandlerResponsibilityChain = new RequestHandlerResponsibilityChain();
    private final AuthenticationRequestHandler authenticationRequestHandler = new AuthenticationRequestHandler();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("Request from client: " + msg);
        Request request = (Request) msg;
        Response response = getResponse(request);
        System.out.println("Response for client: " + response);
        ctx.writeAndFlush(response);
    }

    private Response getResponse(Request request) {
        if (request.commandType() == AUTHENTICATION) return authenticationRequestHandler.handle(request);
        if (authenticationRequestHandler.clientWasAuthenticated()) {
            return requestHandlerResponsibilityChain.handle(request);
        }
        return new Response(ACCESS_DENIED);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.writeAndFlush(new Response(ACCESS_DENIED));
        ctx.close();
    }
}
