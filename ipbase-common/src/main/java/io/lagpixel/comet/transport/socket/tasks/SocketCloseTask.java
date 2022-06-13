package io.lagpixel.comet.transport.socket.tasks;

import io.lagpixel.comet.worker.JobWorker;
import io.lagpixel.comet.worker.Task;

import java.net.ServerSocket;

public final class SocketCloseTask extends Task<Void>{
    private final ServerSocket socket;

    public SocketCloseTask(ServerSocket socket){
        this.socket = socket;
    }

    @Override
    public Void execute(JobWorker worker) throws Throwable {
        if(!socket.isClosed())
            socket.close();

        return null;
    }
}
