package io.lagpixel.comet.worker;

import io.lagpixel.comet.channel.FutureJob;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public interface JobWorker extends ScheduledExecutorService {
    <T> FutureJob<T> schedule(Task<T> task);

    <T> List<FutureJob<T>> invokeAllJobs(@NotNull Collection<? extends Task<T>> tasks) throws InterruptedException;

    FutureJob<Void> schedulePeriodic(RepeatedTask task);

    @Nullable FutureJob<?> getJobById(int id);

    void pauseJob(RepeatedTask task);

    void pauseJob(int id);

    int getIdByJob(Task<?> task);

    void cancelJob(int id, CancellationSource source);

    void cancelJob(Task<?> task, CancellationSource source);

    /**
     * Force shutdown worker. Interrupt all existing task.
     */
    void shutdown();

    /**
     * Shutdown worker gracefully. Worker will wait for all task to end.
     */
    void shutdownGracefully();

}
