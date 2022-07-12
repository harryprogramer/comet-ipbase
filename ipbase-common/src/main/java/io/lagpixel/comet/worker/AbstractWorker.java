package io.lagpixel.comet.worker;

import io.lagpixel.comet.channel.FutureJob;
import io.lagpixel.comet.errors.NotImplementedYet;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public abstract class AbstractWorker implements JobWorker {
    private final static Logger logger = LoggerFactory.getLogger(SimplePeriodicTask.class);

    @Override
    public boolean awaitTermination(long timeout, @NotNull TimeUnit unit) throws InterruptedException {
        throw new NotImplementedYet();
    }

    @NotNull
    @Override
    public <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks, long timeout, @NotNull TimeUnit unit) throws InterruptedException {
        throw new NotImplementedYet();
    }

    @NotNull
    @Override
    public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        throw new NotImplementedYet();
    }

    @Override
    public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks, long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        throw new NotImplementedYet();
    }

    @Override
    public <T> List<FutureJob<T>> invokeAllJobs(@NotNull Collection<? extends Task<T>> tasks) {
        // TODO: not complete implementation
        List<FutureJob<T>> jobs = new ArrayList<>();
        for(var task : tasks){
            jobs.add(schedule(task));
        }

        return jobs;
    }

    @NotNull
    @Override
    public <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks) throws InterruptedException {
        // TODO: not complete implementation
        List<Future<T>> jobs = new ArrayList<>();
        for(var task : tasks){
            jobs.add(schedule(new SimpleTask<>(task)).future());
        }

        return jobs;
    }

    @NotNull
    @Override
    public <T> Future<T> submit(@NotNull Runnable task, T result) {
        Task<T> job = new SimpleTask<>(() -> {task.run(); return null;});
        logger.info("execute(): executing in future task {} for {}", job, task);
        return schedule(job).future();
    }

    @Override
    public void execute(@NotNull Runnable command) {
        Task<Void> job = new SimpleTask<>(() -> {command.run(); return null;});
        logger.info("execute(): executing in future task {} for {}", job, command);
        schedule(job);
    }

    @NotNull
    @Override
    public <T> Future<T> submit(@NotNull Callable<T> task) {
        Task<T> job = new SimpleTask<>(task);
        logger.info("submit(): submitting future task {} for {}", job, task);
        return schedule(job).future();
    }

    @NotNull
    @Override
    public Future<?> submit(@NotNull Runnable task) {
        Task<Void> job = new SimpleTask<>(() -> {task.run(); return null;});
        logger.info("submit(): submitting future task {} for {}", job, task);
        return schedule(job).future();
    }

    @NotNull
    @Override
    public ScheduledFuture<?> schedule(@NotNull Runnable command, long delay, @NotNull TimeUnit unit) {
        Task<Void> job = new SimpleTask<>(() -> {command.run(); return null;}, delay, unit);
        logger.info("schedule(): scheduling future task {} for {}", job, command);
        return schedule(job).scheduledFuture();
    }

    public <V> ScheduledFuture<V> schedule(@NotNull Callable<V> callable,
                                           long delay, @NotNull TimeUnit unit){
        Task<V> job = new SimpleTask<>(callable, delay, unit);
        logger.info("schedule(): scheduling future task {} for {}", job, callable);
        return schedule(job).scheduledFuture();
    }

    public ScheduledFuture<?> scheduleAtFixedRate(@NotNull Runnable command,
                                                  long initialDelay,
                                                  long period,
                                                  @NotNull TimeUnit unit){
        throw new NotImplementedYet(); // TODO: implement scheduleWithFixedDelay
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(@NotNull Runnable command,
                                                     long initialDelay,
                                                     long delay,
                                                     @NotNull TimeUnit unit){
        RepeatedTask periodicJob = new SimplePeriodicTask(command, initialDelay, delay, unit);
        logger.info("schedule(): scheduling periodic task {} for {}", periodicJob, command);
        return schedulePeriodic(periodicJob).scheduledFuture();
    }
}
