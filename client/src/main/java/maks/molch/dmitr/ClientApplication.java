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
import java.util.Objects;

public class ClientApplication {
    private final int serverPort;
    private final String serverHost;
    private final UserInterface userInterface;

    public static void main(String[] args) throws InterruptedException {
        String serverHost = Objects.requireNonNull(System.getenv("SERVER_HOST"));
        int serverPort = Integer.parseInt(Objects.requireNonNull(System.getenv("SERVER_PORT")));
        Path workDirectory = Path.of(Objects.requireNonNull(System.getenv("CLIENT_WORK_DIRECTORY")));
        ClientApplication clientApplication = new ClientApplication(serverPort, serverHost, workDirectory);
        clientApplication.run();
    }

    public ClientApplication(int serverPort, String serverHost, Path workDirectory) {
        this.userInterface = new CommandLineUserInterface(workDirectory);
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    private void run() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(serverHost, serverPort))
                    .handler(getChannelInitializer());
            ChannelFuture future = bootstrap.connect().sync();
            Channel channel = future.channel();
            interactWithUser(channel);
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    private ChannelHandler getChannelInitializer() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ch.pipeline()
                        .addLast(new RequestEncoder(userInterface), new ResponseDecoder(userInterface), new ResponseHandlerProcessing(userInterface));
            }
        };
    }

    private void interactWithUser(Channel channel) {
        userInterface.show("Введите 'exit' для выхода");
        do {
            Request request = userInterface.getRequest();
            channel.writeAndFlush(request);
        } while (!userInterface.wasClosed() && channel.isActive());
        channel.close();
    }
}
