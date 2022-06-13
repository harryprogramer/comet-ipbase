package io.lagpixel.comet.host;

/**
 * Channel listener for handling new connecting.
 * For every new client who connect to server, new initialization of
 * this class is creating.
 */
public interface ChannelListener {
    void newConnection(NetworkClient client);

    void connectionIOError(NetworkClient client, Throwable t);

    void connectionClose(NetworkClient client);
}
