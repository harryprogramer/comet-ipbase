package io.lagpixel.comet.transport.socket;

import com.google.common.base.Joiner;
import io.lagpixel.comet.ServerRuntime;
import io.lagpixel.comet.channel.*;
import io.lagpixel.comet.pipeline.ChannelPipeline;
import io.lagpixel.comet.worker.JobWorker;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.StandardSocketOptions;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

class SocketServerChannelFactory implements ServerChannelFactory {
    private static final AtomicInteger ids = new AtomicInteger();
    private final static Logger logger = LoggerFactory.getLogger(ServerRuntime.class);

    SocketServerChannelFactory(){}

    @Override
    public ServerChannel createNewChannel(JobWorker worker, JobWorker bossWorker, Map<ChannelParameter<?>, Object> options, Class<? extends ChannelHandler> channelListener,
                                          ChannelMessageHandler<?> packetHandler, ChannelPipeline codec) throws IOException {
        Objects.requireNonNull(options, "options");
        Objects.requireNonNull(worker, "worker");
        Objects.requireNonNull(channelListener, "channel handler");
        Objects.requireNonNull(packetHandler, "message handler");
        Objects.requireNonNull(codec, "codec");

        int id =ids.getAndIncrement();
        logger.info("createNewChannel(): creating new socket channel, id=" + id);
        logger.info("channel " + id + ": options -> " + (options.size() != 0 ? Joiner.on(",").withKeyValueSeparator("=").join(options) : "[]"));
        logger.info("channel " + id + ": use worker -> " + worker.getClass().toString());
        logger.info("channel " + id + ": handle channels with -> " + channelListener);
        logger.info("channel " + id + ": handle packets with -> " + packetHandler.getClass().toString());
        logger.info("channel " + id + ": use codec -> " + codec.getClass().toString());

        ServerSocket socket = createSocket(options);
        return new SocketServerChannel(socket, worker, bossWorker);
    }

    @Contract("null -> fail")
    private @NotNull Boolean castBoolean(Object o) {
        if(o instanceof Boolean){
            return (Boolean) o;
        } else {
            throw new ClassCastException("cannot cast " + o.getClass() + " to boolean");
        }
    }
    @Contract("null -> fail")
    private @NotNull Integer castInteger(Object o) {
        if(o instanceof Integer){
            return (Integer) o;
        } else {
            throw new ClassCastException("cannot cast " + o.getClass() + " to integer");
        }
    }

    private void setOptions(@NotNull Map<ChannelParameter<?>, Object> options, ServerSocket socket) throws IOException {
        for (var option : options.entrySet())
            if (option.getValue() == ChannelParameter.TCP_NODELAY) {
                socket.setOption(StandardSocketOptions.TCP_NODELAY, castBoolean(option.getValue()));
            } else if (option.getValue() == ChannelParameter.IP_TOS) {
                socket.setOption(StandardSocketOptions.IP_TOS, castInteger(option.getValue()));
            } else {
                throw new IllegalArgumentException("illegal or unknown channel parameter, [" + option.getKey().name() + "]");
            }
    }

    private @NotNull ServerSocket createSocket(Map<ChannelParameter<?>, Object> options) throws IOException {
        ServerSocket socket = new ServerSocket();
        setOptions(options, socket);
        return socket;
    }

}
