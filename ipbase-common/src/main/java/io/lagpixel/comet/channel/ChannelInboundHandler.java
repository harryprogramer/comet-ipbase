package io.lagpixel.comet.channel;

public interface ChannelInboundHandler<M> extends ChannelHandler {
    void channelRead(Channel channel, M message);
    
}
