package io.lagpixel.comet.transport.socket;

import io.lagpixel.comet.channel.Channel;
import io.lagpixel.comet.channel.ChannelHandler;
import io.lagpixel.comet.worker.JobWorker;
import io.lagpixel.comet.worker.VoidTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Objects;

public class NioSocketHandler extends VoidTask {
    private final Socket socket;
    private final boolean asyncInput;
    private final Channel channel;
    private final JobWorker worker;

    public NioSocketHandler(Socket socket, Channel channel, ChannelHandler listener, JobWorker worker, boolean asyncInput){
        this.socket = Objects.requireNonNull(socket, "socket can't be null");
        this.channel = Objects.requireNonNull(channel, "channel can't be null");
        this.worker = Objects.requireNonNull(worker, "worker can't be null");
        this.asyncInput = asyncInput;
    }

    private void readAsync(ByteBuffer buffer, BufferedInputStream stream) throws IOException {
        //worker.
        while (true) {
            int b = stream.read();
            if (b == -1) {
                break;
            }
            buffer.put((byte) b);
        }
    }

    private void syncForData(InputStream stream) throws IOException{
        while (stream.available() == 0){
            // do nothing, just wait for some available data
        }
    }

    @Override
    public void run(JobWorker worker) throws Throwable {
        try {
            ByteBuffer bf = ByteBuffer.allocate(8192);
            BufferedInputStream stream = new BufferedInputStream(socket.getInputStream());

            while (true) {
                syncForData(stream);

            }
        }catch (IOException e){

        }
    }
}
