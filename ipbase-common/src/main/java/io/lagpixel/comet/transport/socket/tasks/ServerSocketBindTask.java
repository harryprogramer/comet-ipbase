package io.lagpixel.comet.transport.socket.tasks;

import io.lagpixel.comet.channel.ServerChannel;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class ServerSocketBindTask extends AbstractServerSocketTask {
    private final InetSocketAddress address;

    public ServerSocketBindTask(InetSocketAddress address) {
        this.address = address;
    }

    @Override
    public void task() throws Throwable {

    }
}
