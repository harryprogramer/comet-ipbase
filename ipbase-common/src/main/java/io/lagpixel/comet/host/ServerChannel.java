package io.lagpixel.comet.host;

import io.lagpixel.comet.channel.FutureJob;
import io.lagpixel.comet.codec.Packet;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

public interface ServerChannel {
    FutureJob<Void> close() throws IOException;

    boolean isClosed();

    InetAddress getAddress();

    void broadcastMessage(Packet packet);

    List<NetworkClient> getActiveClients();
}
