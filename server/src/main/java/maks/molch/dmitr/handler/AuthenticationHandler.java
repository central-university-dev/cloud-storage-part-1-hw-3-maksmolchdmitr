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
        RequestData requestData = (RequestData) msg;
        ResponseData responseData;
        if (!wasAuthorized) {
            countAttempts++;
            boolean success = authenticator.isAuthenticated(requestData);
            if (success) wasAuthorized = true;
            responseData = new ResponseData(
                    success,
                    (success ? "You successfully authorized!" : "Wrong username or password!"),
                    countAttempts
            );
        } else responseData = new ResponseData(true, "You was already authorized!", countAttempts);
        ctx.writeAndFlush(responseData);
        if (countAttempts == MAX_ATTEMPTS_COUNT) ctx.channel().close();
    }
}
