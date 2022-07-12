package io.lagpixel.comet.channel;

import io.lagpixel.comet.codec.Packet;

import java.net.InetSocketAddress;
import java.util.List;

public interface ServerChannel extends Channel {
    FutureJob<ServerChannel> bind(InetSocketAddress address);

    void broadcast(Packet packet);

    List<Channel> getActiveClients();
}
