package server;

import io.lagpixel.comet.ServerRuntime;
import io.lagpixel.comet.channel.Channel;
import io.lagpixel.comet.channel.ChannelHandler;
import io.lagpixel.comet.transport.socket.SocketServerChannel;
import io.lagpixel.comet.worker.ThreadPoolWorker;
import io.netty.channel.nio.NioEventLoopGroup;

import java.io.IOException;
import java.net.InetSocketAddress;

public class TestServer {
    static class SimpleChannelHandler implements ChannelHandler {

        @Override
        public void newChannel(Channel client) {
            new NioEventLoopGroup().shutdownNow();
        }

        @Override
        public void channelException(Channel client, Throwable t) {

        }

        @Override
        public void channelClosed(Channel client) {

        }
    }
    public static void main(String[] args) throws IOException {
        ServerRuntime serverRuntime = new ServerRuntime();
        serverRuntime.channel(SocketServerChannel.FACTORY)
                .handler(new ChannelMessageHandler<String>() {

            @Override
            public void channelRead(Channel client, String data) {
                System.out.println(client.getRemoteAddress() + ": " + data);
            }

            @Override
            public void channelReadWriteException(Channel client, String data) {

            }
        });

        serverRuntime
            .channelHandler(SimpleChannelHandler.class)
            .worker(new ThreadPoolWorker())
            .channel(SocketServerChannel.FACTORY)
            .bind(new InetSocketAddress(25565));

    }
}
