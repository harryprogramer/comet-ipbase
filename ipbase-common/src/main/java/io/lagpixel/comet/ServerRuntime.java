package io.lagpixel.comet;

import io.lagpixel.comet.codec.PacketCodec;
import io.lagpixel.comet.channel.FutureJob;
import io.lagpixel.comet.errors.BindException;
import io.lagpixel.comet.errors.TransportNotFound;
import io.lagpixel.comet.host.AbstractServerCore;
import io.lagpixel.comet.host.ChannelListener;
import io.lagpixel.comet.host.ChannelPacketHandler;
import io.lagpixel.comet.host.ServerChannel;
import io.lagpixel.comet.channel.ChannelParameter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ServerRuntime {
    private final static Logger logger = LoggerFactory.getLogger(ServerRuntime.class);

    private final Map<ChannelParameter<?>, Object> options = new HashMap<>();
    private Class<? extends ChannelListener> channelListener;
    private ChannelPacketHandler<?> packetHandler;
    private PacketCodec<?> packetCodec;
    private AbstractServerCore kernel;

    public ServerRuntime(ChannelPacketHandler<?> packetHandler, Class<? extends ChannelListener> listener,
            PacketCodec<?> packetCodec) {
        this(packetHandler, listener, packetCodec, null);
    }

    public ServerRuntime(ChannelPacketHandler<?> packetHandler, Class<? extends ChannelListener> listener,
            PacketCodec<?> packetCodec, AbstractServerCore core) {
        this.channelListener = listener;
        this.packetCodec = packetCodec;
        this.packetHandler = packetHandler;

        this.kernel = Objects.requireNonNullElseGet(core, () -> loadCore("io.lagpixel.comet.transport.socket.SocketTransport"));
    }


    public FutureJob<ServerChannel> bind(InetSocketAddress addr) throws IOException {
        if(kernel.isOpen()) {
            logger.warn("bind(): server already bound on {}", kernel.getBindAddress());
            throw new BindException("server already bound");
        }

        if(addr.isUnresolved()){
            logger.error("bind(): inet address is unresolved, aborting bind...");
            throw new BindException("target address unresolved (" + addr + ")");
        }
        logger.info("bind(): bind success on " + addr);
        return kernel.bind(Objects.requireNonNull(addr));
    }

    private static Class<?> loadClass(String name) throws ClassNotFoundException {
        return ServerRuntime.class.getClassLoader().loadClass(name);
    }

    private static @NotNull AbstractServerCore loadCore(String className){
        try {
           Class<?> clazz = loadClass(className);
           if(clazz.isAssignableFrom(AbstractServerCore.class)){
               return (AbstractServerCore) clazz.getDeclaredConstructor().newInstance();
           }else {
               logger.error("loadCore(): transport {} can not be cast", className);
               throw new TransportNotFound("incorrect transport class in " + className);
           }
        }catch (ClassNotFoundException | NoSuchMethodException |
                InstantiationException | IllegalAccessException | InvocationTargetException e){
           logger.error("loadCore(): cannot find transport: " + className);
           throw new TransportNotFound(e.getMessage());
        }
    }

    public void setCore(AbstractServerCore kernel){
        if(kernel != null && kernel.isOpen()){
            logger.error("setCore(): server already running, cannot change core.");
            throw new RuntimeException("core already set and running");
        }
        this.kernel = Objects.requireNonNull(kernel);
    }

    public <T> void addOption(ChannelParameter<T> option, T value){
        Objects.requireNonNull(option, "option is null");
        Objects.requireNonNull(value, "value is null");

        options.put(option, value);
    }
}
