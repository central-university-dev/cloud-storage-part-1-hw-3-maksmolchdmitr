package maks.molch.dmitr.network.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import maks.molch.dmitr.data.RequestData;
import maks.molch.dmitr.data.ResponseData;
import org.springframework.lang.NonNull;

import java.util.Map;

public class AuthenticationHandler extends ChannelInboundHandlerAdapter {
    private final Map<String, String> users = Map.of(
            "Alice", "123",
            "Bob", "password"
    );
    int countAttempts = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, @NonNull Object msg) {
        countAttempts++;
        System.out.println("Count Attempts: " + countAttempts);
        RequestData requestData = (RequestData) msg;
        System.out.println(requestData);
        boolean success = checkUser(requestData);
        ResponseData responseData = new ResponseData(success);
        ctx.writeAndFlush(responseData);
        if (responseData.isSuccess() || countAttempts == 3) ctx.channel().close();
    }

    private boolean checkUser(RequestData requestData) {
        if (!users.containsKey(requestData.username())) return false;
        return users.get(requestData.username()).equals(requestData.password());
    }
}
