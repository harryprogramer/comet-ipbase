package io.lagpixel.comet.transport.socket;

import io.lagpixel.comet.channel.ChannelParameter;
import io.lagpixel.comet.channel.FutureJob;
import io.lagpixel.comet.codec.PacketCodec;
import io.lagpixel.comet.host.AbstractServerCore;
import io.lagpixel.comet.host.ChannelListener;
import io.lagpixel.comet.host.ChannelPacketHandler;
import io.lagpixel.comet.host.ServerChannel;
import io.lagpixel.comet.worker.JobWorker;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

public class SocketTransport extends AbstractServerCore {

    public SocketTransport(JobWorker worker, Map<ChannelParameter<?>, Object> options) {
        super(worker, options);
    }

    @Override
    public void channelListenerUpdate(Class<? extends ChannelListener> channelListener) {

    }


    @Override
    public void packetHandlerUpdate(ChannelPacketHandler<?> channelPacketHandler) {

    }

    @Override
    public void channelPacketCodecUpdate(PacketCodec<?> packetCodec) {

    }

    @Override
    public @NotNull FutureJob<ServerChannel> bind(@NotNull InetSocketAddress addr) throws IOException {
        return null;
    }

    @Override
    public InetSocketAddress getBindAddress() {
        return null;
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public boolean isOpen() {
        return false;
    }
}
