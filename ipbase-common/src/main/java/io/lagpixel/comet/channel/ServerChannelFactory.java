package io.lagpixel.comet.channel;

import io.lagpixel.comet.pipeline.ChannelPipeline;
import io.lagpixel.comet.worker.JobWorker;

import java.io.IOException;
import java.util.Map;

public interface ServerChannelFactory {
    ServerChannel createNewChannel(
            JobWorker worker,
            JobWorker bossWorker,
            Map<ChannelParameter<?>, Object> options,
            Class<? extends ChannelHandler> channelListener,
            ChannelMessageHandler<?> packetHandler,
            ChannelPipeline pipeline
            ) throws IOException;
}
