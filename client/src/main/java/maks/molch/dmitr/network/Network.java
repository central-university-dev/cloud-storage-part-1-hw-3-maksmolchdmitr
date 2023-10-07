package maks.molch.dmitr.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import maks.molch.dmitr.config.ServerConfig;
import maks.molch.dmitr.data.RequestData;
import maks.molch.dmitr.data.ResponseData;
import maks.molch.dmitr.network.handler.ClientInboundHandler;
import maks.molch.dmitr.network.handler.RequestDataEncoder;
import maks.molch.dmitr.network.handler.ResponseDataDecoder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class Network {
    private SocketChannel socketChannel;
    public AtomicBoolean wasConnected = new AtomicBoolean(false);
    public AtomicBoolean wasCrashed = new AtomicBoolean(false);
    private final List<ResponseData> responses = new ArrayList<>();
    private final Semaphore responseSemaphore = new Semaphore(0);

    public Network(ServerConfig serverConfig) {
        new Thread(() -> {
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(@NonNull SocketChannel ch) {
                                socketChannel = ch;
                                ch.pipeline()
                                        .addLast(
                                                new ResponseDataDecoder(),
                                                new RequestDataEncoder(),
                                                new ClientInboundHandler(responses, responseSemaphore)
                                        );
                            }
                        });
                ChannelFuture future = b.connect(serverConfig.host(), serverConfig.port()).sync();
                wasConnected.set(true);
                future.channel().closeFuture().sync();
            } catch (Exception e) {
                wasCrashed.set(true);
                throw new RuntimeException(e);
            } finally {
                workerGroup.shutdownGracefully();
            }
        }).start();
    }

    public void sendRequest(RequestData requestData) {
        socketChannel.writeAndFlush(requestData);
    }

    public ResponseData waitServerResponse() throws InterruptedException {
        responseSemaphore.acquire();
        return responses.get(responses.size() - 1);
    }

    public boolean isAlive() {
        return socketChannel.isActive();
    }

    public boolean wasRunning() {
        return wasConnected.get();
    }

    public void close() {
        socketChannel.close();
    }

    public boolean wasCrashed() {
        return wasCrashed.get();
    }
}
