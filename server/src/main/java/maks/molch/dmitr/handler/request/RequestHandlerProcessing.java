package maks.molch.dmitr.handler.request;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import maks.molch.dmitr.data.request.Request;
import maks.molch.dmitr.data.response.Response;
import maks.molch.dmitr.handler.request.exception.FileNotFoundRuntimeException;
import maks.molch.dmitr.handler.request.exception.LimitAttemptAuthenticationRuntimeException;
import maks.molch.dmitr.handler.request.exception.RequestCanNotBeHandledRuntimeException;

import java.nio.file.Path;

import static maks.molch.dmitr.data.request.CommandType.AUTHENTICATION;
import static maks.molch.dmitr.data.response.ResponseStatus.*;

public class RequestHandlerProcessing extends ChannelInboundHandlerAdapter {
    private final RequestHandlerResponsibilityChain requestHandlerResponsibilityChain;
    private final AuthenticationRequestHandler authenticationRequestHandler = new AuthenticationRequestHandler();

    public RequestHandlerProcessing(Path serverWorkDirectory) {
        this.requestHandlerResponsibilityChain = new RequestHandlerResponsibilityChain(serverWorkDirectory, authenticationRequestHandler);
    }

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
        switch (cause) {
            case LimitAttemptAuthenticationRuntimeException ignored -> {
                ctx.writeAndFlush(new Response(ACCESS_DENIED));
                ctx.close();
            }
            case RequestCanNotBeHandledRuntimeException ignored -> ctx.writeAndFlush(new Response(INVALID_REQUEST));
            case FileNotFoundRuntimeException ignored -> ctx.writeAndFlush(new Response(FILE_NOT_FOUND));
            default -> ctx.writeAndFlush(new Response(SERVER_ERROR, new String[]{cause.toString()}));
        }
    }
}
