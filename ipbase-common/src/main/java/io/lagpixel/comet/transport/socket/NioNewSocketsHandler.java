package io.lagpixel.comet.transport.socket;

import io.lagpixel.comet.channel.Channel;
import io.lagpixel.comet.channel.ChannelHandler;
import io.lagpixel.comet.worker.JobWorker;
import io.lagpixel.comet.worker.RepeatedTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

final class NioNewSocketsHandler extends RepeatedTask {
    private final static Logger logger = LoggerFactory.getLogger(NioNewSocketsHandler.class);
    private final ServerSocket socket;
    private final ChannelHandler listener;

    private NioNewSocketsHandler(int connectionPerSecond, ServerSocket socket, ChannelHandler listener) {
        super(connectionPerSecond, 1, TimeUnit.NANOSECONDS);
        this.socket = socket;
        this.listener = listener;
    }

    @Override
    public void periodTick(JobWorker worker) throws Throwable {
        int bufferSize = 8192;
        Channel channel = null;
        try {
            Socket newChannel = socket.accept();
            if(newChannel != null){
                channel = new SocketChannel(newChannel, worker);
                ByteBuffer bf = ByteBuffer.allocate(bufferSize);
                BufferedInputStream inFromClient = new BufferedInputStream(newChannel.getInputStream());
                while (true) {
                    int b = inFromClient.read();
                    if (b == -1) {
                        break;
                    }
                    bf.put((byte) b);
                }
            }else {
                if(socket.isClosed()){
                    logger.warn("accept(): socket closed unexpectedly, unbinding {}", socket.getInetAddress());
                }
                logger.error("accept(): return null, unbinding {}", socket.getInetAddress());
                socket.close();
            }
        }catch (IOException e){
            listener.channelException(null, e);
        }
    }
}

