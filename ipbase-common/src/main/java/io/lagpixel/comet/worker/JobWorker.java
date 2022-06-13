package io.lagpixel.comet.worker;

import io.lagpixel.comet.channel.FutureJob;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface JobWorker {
    @NotNull <T> FutureJob<T> scheduleJob(Task<T> task);

    @Nullable FutureJob<?> getJobById(int id);

    void cancelJob(int id);

    void cancelJob(Task<?> task);

    /**
     * Force shutdown worker. Interrupt all existing task.
     */
    void shutdown();

    /**
     * Shutdown worker gracefully. Worker will wait for all task to end.
     */
    void shutdownGracefully();
}
