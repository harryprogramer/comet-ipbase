package io.lagpixel.comet.host;

public interface ChannelPacketHandler<T> {
    void onData(NetworkClient client, T data);

    void onDataIOError(NetworkClient client, T data);
}
