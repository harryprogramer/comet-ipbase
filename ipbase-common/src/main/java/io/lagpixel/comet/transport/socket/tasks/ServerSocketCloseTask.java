package io.lagpixel.comet.transport.socket.tasks;

import io.lagpixel.comet.channel.Channel;
import io.lagpixel.comet.channel.ServerChannel;
import io.lagpixel.comet.worker.JobWorker;
import io.lagpixel.comet.worker.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;

public final class ServerSocketCloseTask extends Task<Channel>{
    private final static Logger logger = LoggerFactory.getLogger(ServerSocketCloseTask.class);
    private final ServerChannel channel;
    private final ServerSocket socket;

    public ServerSocketCloseTask(ServerSocket socket, ServerChannel channel) {
        this.socket = socket;
        this.channel = channel;
    }

    @Override
    public Channel execute(JobWorker worker) throws Throwable {
        if(channel.isClosed()){
            throw new IOException("server already closed");
        }
        logger.info("close() -> " + channel.getRemoteAddress() + " closing socket.");
        if(!socket.isClosed()) {
            socket.close();
        }

        return channel;
    }
}
