package io.lagpixel.comet.host;

import java.net.InetSocketAddress;

public interface NetworkClient {
    InetSocketAddress getAddress();

    boolean isConnected();

    void disconnect();
}
