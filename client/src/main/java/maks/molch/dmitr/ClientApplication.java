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

public class ClientApplication {
    private final int SERVER_PORT;
    private final String SERVER_HOST;
    private final UserInterface userInterface;

    public static void main(String[] args) throws InterruptedException {
        checkArgs(args);
        String serverHost = args[0];
        int serverPort = Integer.parseInt(args[1]);
        Path workDirectory = Path.of(args[2]);
        ClientApplication clientApplication = new ClientApplication(serverPort, serverHost, workDirectory);
        clientApplication.run();
    }

    public ClientApplication(int serverPort, String serverHost, Path workDirectory) {
        this.userInterface = new CommandLineUserInterface(workDirectory);
        SERVER_HOST = serverHost;
        SERVER_PORT = serverPort;
    }

    private static void checkArgs(String[] args) {
        if (args.length == 3) return;
        System.err.println("Usage: java Main <serverHost> <serverPort> <workDirectory>");
        System.exit(-1);
    }

    private void run() throws InterruptedException {
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
