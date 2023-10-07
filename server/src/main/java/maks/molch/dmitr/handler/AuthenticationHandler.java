package maks.molch.dmitr.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import maks.molch.dmitr.data.RequestData;
import maks.molch.dmitr.data.ResponseData;
import maks.molch.dmitr.service.Authenticator;
import org.springframework.lang.NonNull;

public class AuthenticationHandler extends ChannelInboundHandlerAdapter {
    private static final int MAX_ATTEMPTS_COUNT = 3;
    private int countAttempts = 0;
    private final Authenticator authenticator;
    private boolean wasAuthorized = false;

    public AuthenticationHandler(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    public void channelRead(@NonNull ChannelHandlerContext ctx, @NonNull Object msg) {
        if (!wasAuthorized) countAttempts++;
        System.out.println("Count Attempts: " + countAttempts);
        RequestData requestData = (RequestData) msg;
        System.out.println(requestData);
        boolean success = authenticator.isAuthenticated(requestData);
        ResponseData responseData = getResponseData(success);
        ctx.writeAndFlush(responseData);
        if (success) wasAuthorized = true;
        if (countAttempts == MAX_ATTEMPTS_COUNT) ctx.channel().close();
    }

    private ResponseData getResponseData(boolean success) {
        ResponseData responseData;
        if (wasAuthorized) {
            responseData = new ResponseData(success, "You was already authorized!", countAttempts);
        } else {
            responseData = new ResponseData(
                    success,
                    (success ? "You successfully authorized!" : "Wrong username or password!"),
                    countAttempts
            );
        }
        return responseData;
    }
}
