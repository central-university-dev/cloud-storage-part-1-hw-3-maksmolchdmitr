package maks.molch.dmitr;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import maks.molch.dmitr.data.request.Request;
import maks.molch.dmitr.handler.decode.ResponseDecoder;
import maks.molch.dmitr.handler.encode.RequestEncoder;
import maks.molch.dmitr.handler.response.ResponseHandlerProcessing;
import maks.molch.dmitr.interaction.CommandLineUserInterface;
import maks.molch.dmitr.interaction.UserInterface;

import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClientApplication {
    private static final int SERVER_PORT = Integer.parseInt(System.getenv("SERVER_PORT"));
    private static final String SERVER_HOST = System.getenv("SERVER_HOST");
    private static final Path workDirectory = Paths.get(".");
    private static final UserInterface userInterface = new CommandLineUserInterface(workDirectory);

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(SERVER_HOST, SERVER_PORT))
                    .handler(getChannelInitializer());
            ChannelFuture future = bootstrap.connect().sync();
            Channel channel = future.channel();
            interactWithUser(channel);
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    private static void interactWithUser(Channel channel) {
        userInterface.show("Введите 'exit' для выхода");
        do {
            Request request = userInterface.getRequest();
            channel.writeAndFlush(request);
        } while (!userInterface.wasClosed() && channel.isActive());
        channel.close();
    }

    private static ChannelHandler getChannelInitializer() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ch.pipeline()
                        .addLast(new RequestEncoder(userInterface), new ResponseDecoder(userInterface), new ResponseHandlerProcessing(userInterface));
            }
        };
    }
}
