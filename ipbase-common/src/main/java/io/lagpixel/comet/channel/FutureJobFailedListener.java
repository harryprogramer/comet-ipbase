package io.lagpixel.comet.channel;

@FunctionalInterface
public interface FutureJobFailedListener<T>{
    void jobFailed(Throwable t, FutureJob<T> task);
}
