package io.lagpixel.comet.transport.socket.tasks;

import io.lagpixel.comet.channel.Channel;
import io.lagpixel.comet.worker.JobWorker;
import io.lagpixel.comet.worker.Task;

import java.net.Socket;

public class CloseSocketTask extends Task<Channel> {
    private final Socket socket;

    public CloseSocketTask(Socket socket){
        this.socket = socket;
    }
    @Override
    public Channel execute(JobWorker worker) throws Throwable {
        return null;
    }
}
