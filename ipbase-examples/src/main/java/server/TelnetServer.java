package server;

        //package io.netty.example.telnet;

        import io.lagpixel.comet.channel.Channel;
        import io.netty.bootstrap.ServerBootstrap;
        import io.netty.channel.ChannelFuture;
        import io.netty.channel.ChannelPipeline;
        import io.netty.channel.EventLoopGroup;
        import io.netty.channel.nio.NioEventLoopGroup;
        import io.netty.channel.socket.nio.NioServerSocketChannel;
        import io.netty.handler.logging.LogLevel;
        import io.netty.handler.logging.LoggingHandler;
        import io.netty.handler.ssl.SslContext;
        import io.netty.handler.ssl.SslContextBuilder;
        import io.netty.handler.ssl.util.SelfSignedCertificate;

        import java.util.concurrent.TimeUnit;

/**
 * Simplistic telnet server.
 */
public final class TelnetServer {

    static final boolean SSL = System.getProperty("ssl") != null;
    static final int PORT = Integer.parseInt(System.getProperty("port", SSL? "8992" : "8023"));

    public static void main(String[] args) throws Exception {
        // Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(1);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new TelnetServerInitializer(sslCtx));
            ChannelFuture channel = b.bind(PORT).sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}