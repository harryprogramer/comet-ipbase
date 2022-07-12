package io.lagpixel.comet.worker;

import io.lagpixel.comet.channel.FutureJob;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;

public final class ThreadPoolWorker extends AbstractWorker {
    private final static Logger logger = LoggerFactory.getLogger(ThreadPoolWorker.class);
    private final int nThreads;

    private final ScheduledExecutorService pool;

    public ThreadPoolWorker(){
        this(1);
    }

    public ThreadPoolWorker(int nThreads){
        if(nThreads < 0 || nThreads == 0){
            throw new IllegalStateException("thread count must be higher than 0");
        }
        this.nThreads = nThreads;
        this.pool = Executors.newScheduledThreadPool(nThreads, ThreadCometFactory.INSTANCE);
    }

    static class CachedThreadPoolTask<T> implements FutureJob<T> {
        private boolean error = false;
        private Future<T> future;
        private final int id;
        private Throwable t;

        public CachedThreadPoolTask(int id){
            this.id = id;
        }

        protected void setFuture(Future<T> future){
            this.future = future;
        }

        protected void throwError(Throwable t){
            this.error = true;
            this.t = t;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public boolean isCancelled() {
            return future.isCancelled();
        }

        @Override
        public boolean isDone() {
            return future.isDone();
        }

        @Override
        public boolean isSuccess() {
            return !error;
        }

        @Override
        public Throwable getCause() {
            if(!isDone()){
                logger.warn("getCause() -> job id: " + getId() + " is not yet completed");
                throw new IllegalStateException("the task has not yet been completed");
            }
            if(isSuccess()){
                logger.warn("getCause() -> job id: " + getId() + " is finished successfully, not error was thrown");
                throw new IllegalStateException("task was finished successfully");
            }

            return t;
        }

        @Override
        public void cancel(boolean isInterrupt) {
            future.cancel(isInterrupt);
        }

        @Override
        public T get() throws ExecutionException, InterruptedException {
            return future.get();
        }

        @Override
        public Task<T> getTask() {
            return null;
        }

        @Override
        public ScheduledFuture<T> scheduledFuture() {
            return null;
        }

        @Override
        public Future<T> future() {
            return FutureJob.super.future();
        }
    }

    @Override
    public @NotNull <T> FutureJob<T> schedule(Task<T> task) {
        CachedThreadPoolTask<T> threadTask = new CachedThreadPoolTask<>(task.getID());
        JobWorker instance = this;

        Callable<T> callable = () -> {
            try {
                return task.execute(instance);
            } catch (Throwable t) {
                logger.warn("scheduleJob() -> pool.submit(): task " + threadTask.getId() + ", throw error " + t.getClass());
                threadTask.throwError(t);
                return null;
            }
        };

        Future<T> future;
        if(task.delay < 0){
            future = pool.submit(callable);
        }else {
            future = pool.schedule(callable, task.delay, task.unit);
        }

        threadTask.setFuture(future);

        return threadTask;
    }


    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public FutureJob<Void> schedulePeriodic(RepeatedTask task) {
        logger.info("schedulePeriodic(): scheduling periodic task {}", task);
        Future future = pool.scheduleAtFixedRate(() -> {
            try {
                task.periodTick(this);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }, task.delay, task.period, task.unit);

        CachedThreadPoolTask<?> job = new CachedThreadPoolTask<>(task.getID());
        job.setFuture(future);
        return (FutureJob<Void>) job;
    }

    @Override
    public @Nullable FutureJob<?> getJobById(int id) {
        return null;
    }

    @Override
    public void pauseJob(RepeatedTask task) {

    }

    @Override
    public void pauseJob(int id) {

    }

    @Override
    public int getIdByJob(Task<?> task) {
        return 0;
    }

    @Override
    public void cancelJob(int id, CancellationSource source) {

    }

    @Override
    public void cancelJob(Task<?> task, CancellationSource source) {

    }


    @Override
    public void shutdown() {
        pool.shutdown();
        pool.shutdownNow();
    }

    @NotNull
    @Override
    public List<Runnable> shutdownNow() {
        return null;
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean awaitTermination(long timeout, @NotNull TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void shutdownGracefully() {

    }

    public int getThreadsCount() {
        return nThreads;
    }
}
