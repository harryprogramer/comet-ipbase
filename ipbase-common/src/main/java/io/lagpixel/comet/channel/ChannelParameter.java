package io.lagpixel.comet.channel;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public interface ChannelParameter<T> {
    String name();

    ChannelParameter<Integer> TCP_NODELAY = valueOf("TCP_NODELAY");

    static @NotNull <T> ChannelParameter<T> valueOf(@NotNull String name){
        Objects.requireNonNull(name);
        return () -> name;
    }
}
