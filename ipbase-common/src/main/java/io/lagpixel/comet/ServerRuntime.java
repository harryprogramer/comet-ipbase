package io.lagpixel.comet;

import io.lagpixel.comet.channel.FutureJob;
import io.lagpixel.comet.errors.BindException;
import io.lagpixel.comet.channel.ChannelHandler;
import io.lagpixel.comet.channel.ServerChannel;
import io.lagpixel.comet.channel.ChannelParameter;
import io.lagpixel.comet.channel.ServerChannelFactory;
import io.lagpixel.comet.pipeline.ChannelPipeline;
import io.lagpixel.comet.pipeline.DefaultChannelPipeline;
import io.lagpixel.comet.transport.socket.SocketServerChannel;
import io.lagpixel.comet.worker.JobWorker;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ServerRuntime {
    private final static Logger logger = LoggerFactory.getLogger(ServerRuntime.class);

    private final Map<ChannelParameter<?>, Object> options = new HashMap<>();
    private Class<? extends ChannelHandler> channelListener;
    private ChannelMessageHandler<?> packetHandler;
    private ServerChannelFactory factory;

    private final ChannelPipeline pipeline = new DefaultChannelPipeline();
    private ServerChannel channel;
    private JobWorker bossWorker;
    private JobWorker worker;

    private void validate(){
        Objects.requireNonNull(channelListener, "no channel handler was set");
        Objects.requireNonNull(packetHandler, "no packet handler was set");
        Objects.requireNonNull(worker, "no worker was set");
        Objects.requireNonNull(bossWorker, "bossWorker was not set");
    }

    private ServerChannel createTransport(@NotNull ServerChannelFactory factory) throws IOException {
        return Objects.requireNonNull(factory.createNewChannel(worker, bossWorker,options,
                        channelListener, packetHandler, pipeline),
                "factory create null instance");
    }

    public ChannelPipeline datapipe(){
        return pipeline;
    }

    public @NotNull ServerRuntime channelHandler(Class<? extends ChannelHandler> listener){
        this.channelListener = Objects.requireNonNull(listener);
        return this;
    }

    public @NotNull ServerRuntime handler(ChannelMessageHandler<?> handler){
        this.packetHandler = Objects.requireNonNull(handler);
        return this;
    }

    public ServerRuntime worker(JobWorker worker){
        this.worker = Objects.requireNonNull(worker);
        return this;
    }

    public ServerRuntime bossWorker(JobWorker worker){
        this.bossWorker = Objects.requireNonNull(worker);
        return this;
    }

    @NotNull
    public FutureJob<ServerChannel> bind(InetSocketAddress addr) throws IOException {
        try{
            validate();
        }catch (NullPointerException e){
            logger.error("bind(): runtime configuration not complete, look exception message");
            throw e;
        }

        if(factory == null){
            logger.info("no channel specified for {}, selecting default: {}", this.getClass(), SocketServerChannel.FACTORY);
            this.factory = SocketServerChannel.FACTORY;
        }
        this.channel = createTransport(factory);
        if(!channel.isClosed()) {
            logger.warn("bind(): server already bound on {}", channel.getRemoteAddress());
            throw new BindException("server already bound");
        }

        if(addr.isUnresolved()){
            logger.error("bind(): inet address is unresolved, aborting bind...");
            throw new BindException("target address unresolved (" + addr + ")");
        }
        logger.info("bind(): bind success on " + addr);
        return channel.bind(Objects.requireNonNull(addr));
    }

    private static Class<?> loadClass(String name) throws ClassNotFoundException {
        return ServerRuntime.class.getClassLoader().loadClass(name);
    }

    public ServerRuntime channel(ServerChannelFactory channelFactory){
        if(channel != null && !channel.isClosed()){
            logger.error("setCore(): server already running, cannot change core.");
            throw new RuntimeException("core already set and running");
        }
        this.factory = Objects.requireNonNull(channelFactory);
        return this;
    }

    public <T> void addOption(ChannelParameter<T> option, T value){
        Objects.requireNonNull(option, "option is null");
        Objects.requireNonNull(value, "value is null");
        options.put(option, value);
    }

    public void deleteOption(ChannelParameter<?> option){
        options.remove(option);
    }
}
