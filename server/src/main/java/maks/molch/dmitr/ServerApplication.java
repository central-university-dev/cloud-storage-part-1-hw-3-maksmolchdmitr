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
import java.util.Objects;

public class ServerApplication {
    private static final int SO_BACKLOG_OPTION_VALUE = 128;

    private final int serverPort;
    private final Path workDirectory;

    public ServerApplication(int serverPort, Path workDirectory) {
        this.serverPort = serverPort;
        this.workDirectory = workDirectory;
    }

    public static void main(String[] args) throws InterruptedException {
        int serverPort = Integer.parseInt(Objects.requireNonNull(System.getenv("SERVER_PORT")));
        Path workDirectory = Path.of(Objects.requireNonNull(System.getenv("SERVER_WORK_DIRECTORY")));
        ServerApplication serverApplication = new ServerApplication(serverPort, workDirectory);
        serverApplication.run();
    }

    public void run() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(getChannelInitializer())
                    .option(ChannelOption.SO_BACKLOG, SO_BACKLOG_OPTION_VALUE)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = bootstrap.bind(serverPort).sync();
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
