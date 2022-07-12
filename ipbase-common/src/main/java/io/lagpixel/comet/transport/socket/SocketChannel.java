package io.lagpixel.comet.transport.socket;

import io.lagpixel.comet.channel.Channel;
import io.lagpixel.comet.channel.ChannelParameter;
import io.lagpixel.comet.channel.FutureJob;
import io.lagpixel.comet.errors.NotImplementedYet;
import io.lagpixel.comet.transport.socket.tasks.SocketCloseTask;
import io.lagpixel.comet.worker.JobWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;
import java.net.SocketAddress;
import java.util.Map;

class SocketChannel implements Channel {
    private final static Logger logger = LoggerFactory.getLogger(NioNewSocketsHandler.class);
    private final Socket socket;
    private final JobWorker worker;

    protected SocketChannel(Socket socket, JobWorker worker){
        this.socket = socket;
        this.worker = worker;
    }

    @Override
    public Map<ChannelParameter<?>, Object> getChannelOptions() {
        throw new NotImplementedYet(); // TODO
    }

    @Override
    public SocketAddress getRemoteAddress() {
        return socket.getRemoteSocketAddress();
    }

    @Override
    public SocketAddress getLocalAddress() {
        return socket.getLocalSocketAddress();
    }

    @Override
    public boolean isActive() {
        return !socket.isClosed();
    }

    @Override
    public FutureJob<Channel> close() {
        SocketCloseTask task = new SocketCloseTask();
        task.socket(socket);
        task.channel(this);
        return worker.schedule(task);
    }
}
