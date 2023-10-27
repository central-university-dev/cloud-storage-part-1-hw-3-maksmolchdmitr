package maks.molch.dmitr;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import maks.molch.dmitr.handler.decode.RequestDecoder;
import maks.molch.dmitr.handler.encode.ResponseEncoder;
import maks.molch.dmitr.handler.request.RequestHandlerProcessing;

import java.nio.file.Path;

public class ServerApplication {
    private final int SERVER_PORT;
    private final Path workDirectory;

    public ServerApplication(int serverPort, Path workDirectory) {
        SERVER_PORT = serverPort;
        this.workDirectory = workDirectory;
    }

    public static void main(String[] args) throws InterruptedException {
        checkArgs(args);
        int serverPort = Integer.parseInt(args[0]);
        Path workDirectory = Path.of(args[1]);
        ServerApplication serverApplication = new ServerApplication(serverPort, workDirectory);
        serverApplication.run();
    }

    private static void checkArgs(String[] args) {
        if (args.length == 2) return;
        System.err.println("Usage: java Main <serverPort> <workDirectory>");
        System.exit(-1);
    }

    public void run() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(getChannelInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = bootstrap.bind(SERVER_PORT).sync();
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private ChannelInitializer<SocketChannel> getChannelInitializer() {
        return new ChannelInitializer<>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ch.pipeline()
                        .addLast(new RequestDecoder(), new ResponseEncoder(), new RequestHandlerProcessing(workDirectory));
            }
        };
    }
}
