package io.lagpixel.comet.channel;

@FunctionalInterface
public interface FutureJobListener<T> {
    void jobEnded(T job);
}
