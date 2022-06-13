package server;

import io.lagpixel.comet.ServerRuntime;
import io.lagpixel.comet.channel.FutureJob;
import io.lagpixel.comet.host.ServerChannel;

import java.io.IOException;
import java.net.InetSocketAddress;

public class EchoServer {
    public static void main(String[] args) throws IOException {
        ServerRuntime runtime = new ServerRuntime(null, null, null);
        FutureJob<ServerChannel> channel =  runtime.bind(new InetSocketAddress(2090));
    }
}
