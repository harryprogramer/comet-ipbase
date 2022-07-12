package io.lagpixel.comet.channel;

import java.net.SocketAddress;
import java.util.Map;

public interface Channel {
    Map<ChannelParameter<?>, Object> getChannelOptions();

    SocketAddress getRemoteAddress();

    SocketAddress getLocalAddress();

    boolean isActive();

    default boolean isClosed(){
        return !isActive();
    }

    FutureJob<Channel> close();
}
