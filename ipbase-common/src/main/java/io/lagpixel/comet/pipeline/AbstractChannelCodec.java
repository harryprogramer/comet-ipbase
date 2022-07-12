package io.lagpixel.comet.pipeline;

import io.lagpixel.comet.channel.Channel;
import io.lagpixel.comet.channel.ChannelCodec;
import io.lagpixel.comet.common.Ignore;

public abstract class AbstractChannelCodec<I, O> implements ChannelCodec<I, O> {
    @Ignore
    @Override
    public void newChannel(Channel client) {
        //NOOP
    }

    @Ignore
    @Override
    public void channelClosed(Channel client) {
        //NOOP
    }
    @Ignore
    @Override
    public void channelException(Channel client, Throwable t) {
        //NOOP
    }

}
