package io.lagpixel.comet.host;

import io.lagpixel.comet.codec.PacketCodec;
import io.lagpixel.comet.channel.FutureJob;

import io.lagpixel.comet.channel.ChannelParameter;
import io.lagpixel.comet.worker.JobWorker;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

public abstract class AbstractServerCore implements Closeable, AutoCloseable {
    private final Map<ChannelParameter<?>, Object> options;
    private final JobWorker worker;

    public AbstractServerCore(JobWorker worker, Map<ChannelParameter<?>, Object> options){
        this.options = options;
        this.worker = worker;
    }

    public abstract void channelListenerUpdate(Class<? extends ChannelListener> channelListener);

    public abstract void packetHandlerUpdate(ChannelPacketHandler<?> channelPacketHandler);

    public abstract void channelPacketCodecUpdate(PacketCodec<?> packetCodec);

    @NotNull
    public abstract FutureJob<ServerChannel> bind(@NotNull InetSocketAddress addr) throws IOException;

    public abstract InetSocketAddress getBindAddress();

    public abstract void close() throws IOException;

    public abstract boolean isOpen();

}
