package maks.molch.dmitr;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import maks.molch.dmitr.config.ServerConfig;
import maks.molch.dmitr.handler.AuthenticationHandler;
import maks.molch.dmitr.handler.RequestDecoder;
import maks.molch.dmitr.handler.ResponseEncoder;
import maks.molch.dmitr.service.Authenticator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;

@EnableConfigurationProperties(ServerConfig.class)
@SpringBootApplication
public class ServerApplication {
    private final ServerConfig serverConfig;
    private final Authenticator authenticator;

    public ServerApplication(ServerConfig serverConfig, Authenticator authenticator) {
        this.serverConfig = serverConfig;
        this.authenticator = authenticator;
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    public void run() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(@NonNull SocketChannel ch) {
                            ch.pipeline()
                                    .addLast(new RequestDecoder(), new ResponseEncoder(), new AuthenticationHandler(authenticator));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = bootstrap.bind(serverConfig.port()).sync();
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
