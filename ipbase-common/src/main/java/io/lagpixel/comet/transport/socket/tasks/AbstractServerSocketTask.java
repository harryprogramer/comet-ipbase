package io.lagpixel.comet.transport.socket.tasks;

import io.lagpixel.comet.channel.Channel;
import io.lagpixel.comet.channel.ServerChannel;
import io.lagpixel.comet.worker.JobWorker;
import io.lagpixel.comet.worker.Task;

import java.net.ServerSocket;
import java.util.Objects;

public abstract class AbstractServerSocketTask extends Task<ServerChannel> {
    private ServerChannel channel;
    private ServerSocket socket;

    @Override
    public ServerChannel execute(JobWorker worker) throws Throwable {
        validate();
        task();
        return channel;
    }

    public void validate() throws NullPointerException{
        Objects.requireNonNull(socket, "socket task " + this.getClass() + " has no set socket");
        Objects.requireNonNull(channel, "socket task " + this.getClass() + " has no set channel");
    }

    public ServerSocket socket() {
        return socket;
    }

    public ServerChannel channel() {
        return channel;
    }

    public void channel(ServerChannel channel){
        this.channel = Objects.requireNonNull(channel);
    }

    public void socket(ServerSocket socket){
        this.socket = Objects.requireNonNull(socket);
    }

    public abstract void task() throws Throwable;
}