package io.lagpixel.comet.channel;

import io.lagpixel.comet.common.Ignore;

import java.io.IOException;

public interface ChannelCodec<I, O> extends ChannelHandler {
    O input(I msg) throws IOException;

    I output(O msg) throws IOException;
}
