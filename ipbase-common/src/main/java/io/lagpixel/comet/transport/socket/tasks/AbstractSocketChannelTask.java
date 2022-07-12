package io.lagpixel.comet.transport.socket.tasks;

import io.lagpixel.comet.channel.Channel;
import io.lagpixel.comet.channel.ServerChannel;
import io.lagpixel.comet.worker.JobWorker;
import io.lagpixel.comet.worker.Task;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public abstract class AbstractSocketChannelTask extends Task<Channel> {
    private Channel channel;
    private Socket socket;

    @Override
    public Channel execute(JobWorker worker) throws Throwable {
        validate();
        task();
        return channel;
    }

    public Socket socket() {
        return socket;
    }

    public Channel channel() {
        return channel;
    }

    public void socket(Socket socket) {
        this.socket = Objects.requireNonNull(socket);
    }

    public void channel(Channel channel) {
        this.channel = Objects.requireNonNull(channel);
    }

    public void validate() throws NullPointerException {
        Objects.requireNonNull(socket);
        Objects.requireNonNull(channel);
    }

    public abstract void task() throws Throwable;
}
