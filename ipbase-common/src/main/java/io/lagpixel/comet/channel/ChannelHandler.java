package io.lagpixel.comet.channel;

/**
 * Channel listener for handling new connecting.
 * For every new client who connect to server, new initialization of
 * this class is creating.
 */
public interface ChannelHandler {
    void newChannel(Channel client);

    void channelException(Channel client, Throwable t);

    void channelClosed(Channel client);
}
