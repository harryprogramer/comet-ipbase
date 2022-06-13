package io.lagpixel.comet.transport.socket;

import io.lagpixel.comet.channel.FutureJob;
import io.lagpixel.comet.codec.Packet;
import io.lagpixel.comet.host.NetworkClient;
import io.lagpixel.comet.host.ServerChannel;
import io.lagpixel.comet.transport.socket.tasks.SocketCloseTask;
import io.lagpixel.comet.worker.JobWorker;
import io.lagpixel.comet.worker.SimpleCallableTask;
import io.lagpixel.comet.worker.SimpleTask;
import io.lagpixel.comet.worker.Task;
import io.netty.channel.ChannelFuture;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.Callable;

public class SocketServerChannel implements ServerChannel {
    private final JobWorker worker;
    private final ServerSocket socket;

    protected SocketServerChannel(ServerSocket socket, JobWorker worker){
        this.worker = worker;
        this.socket = socket;
    }

    @Override
    public FutureJob<Void> close() throws IOException {
        if(isClosed()){
            throw new IOException("server already closed");
        }
        return worker.scheduleJob(new SocketCloseTask(socket));
    }

    @Override
    public boolean isClosed() {
        return socket.isClosed();
    }

    @Override
    public InetAddress getAddress() {
        return null;
    }

    @Override
    public void broadcastMessage(Packet packet) {

    }

    @Override
    public List<NetworkClient> getActiveClients() {
        return null;
    }
}
