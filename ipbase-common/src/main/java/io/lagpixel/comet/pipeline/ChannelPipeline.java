package io.lagpixel.comet.pipeline;

import io.lagpixel.comet.channel.ChannelCodec;
import io.lagpixel.comet.channel.ChannelHandler;
import io.lagpixel.comet.common.Listable;

import javax.annotation.Nullable;

public interface ChannelPipeline extends Listable<ChannelHandler> {

    void add(DedicatedPipelineOrder order, ChannelCodec<?, ?> codec);

    void addNext(ChannelHandler codec);

    void addNext(@Nullable String name, ChannelHandler codec);

    void remove(String name);

    void remove(Class<? extends ChannelHandler> codec);

    void removeLast();

    void removeFirst();

    @Nullable
    ChannelHandler get(String name);
}
