package io.lagpixel.comet.transport.socket;

import io.lagpixel.comet.channel.*;
import io.lagpixel.comet.codec.Packet;
import io.lagpixel.comet.transport.socket.tasks.ServerSocketBindTask;
import io.lagpixel.comet.transport.socket.tasks.ServerSocketCloseTask;
import io.lagpixel.comet.worker.JobWorker;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SocketServerChannel implements ServerChannel {
    public static final ServerChannelFactory FACTORY = new SocketServerChannelFactory();
    private final ServerSocket socket;
    private final JobWorker worker;

    private final JobWorker bossWorker;

    protected SocketServerChannel(ServerSocket socket, JobWorker worker, JobWorker bossWorker) {
        this.worker = Objects.requireNonNull(worker, "worker");
        this.socket = Objects.requireNonNull(socket, "socket");
        this.bossWorker = Objects.requireNonNull(bossWorker, "boss worker");
    }

    @Override
    public FutureJob<ServerChannel> bind(InetSocketAddress address) {
        ServerSocketBindTask task = new ServerSocketBindTask(address);
        task.channel(this);
        task.socket(socket);
        return bossWorker.schedule(task);
    }

    @Override
    public Map<ChannelParameter<?>, Object> getChannelOptions() {
        return null;
    }

    @Override
    public SocketAddress getRemoteAddress() {
        return null;
    }

    @Override
    public SocketAddress getLocalAddress() {
        return null;
    }

    @Override
    public FutureJob<Channel> close() {
        return bossWorker.schedule(new ServerSocketCloseTask(socket, this));
    }

    @Override
    public boolean isActive() {
        return socket.isClosed();
    }

    @Override
    public void broadcast(Packet packet) {

    }

    @Override
    public List<Channel> getActiveClients() {
        return null;
    }
}
