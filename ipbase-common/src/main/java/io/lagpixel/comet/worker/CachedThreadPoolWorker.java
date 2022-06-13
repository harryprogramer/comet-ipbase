package io.lagpixel.comet.worker;

import io.lagpixel.comet.channel.FutureJob;
import io.lagpixel.comet.channel.FutureJobFailedListener;
import io.lagpixel.comet.channel.FutureJobListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CachedThreadPoolWorker implements JobWorker {

    static { // check if there is a custom maxThreadPool setting.
        String key = System.getProperty("comet.io.worker.cachedthreadpool.maxThreadPool");
        if(key == null){ // if there is not, set default value to 0
            System.setProperty("comet.io.worker.cachedthreadpool.maxThreadPool",
                    String.valueOf(0));
        }
    }

    private final static Logger logger = LoggerFactory.getLogger(CachedThreadPoolWorker.class);
    private final ScheduledExecutorService scheduledExecutor =
            Executors.newScheduledThreadPool(Integer.parseInt(System.getProperty("comet.io.worker.cachedthreadpool.maxThreadPool")));
    private final ExecutorService pool = Executors.newCachedThreadPool();
    private final AtomicInteger ids = new AtomicInteger();

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
    }

    @Override
    public @NotNull <T> FutureJob<T> scheduleJob(Task<T> task) {
        CachedThreadPoolTask<T> threadTask = new CachedThreadPoolTask<>(ids.getAndIncrement());
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

        Future<T> future = pool.submit(callable);

        threadTask.setFuture(future);

        return threadTask;
    }

    @Override
    public @Nullable FutureJob<?> getJobById(int id) {
        return null;
    }

    @Override
    public void cancelJob(int id) {

    }

    @Override
    public void cancelJob(Task<?> task) {

    }

    @Override
    public void shutdown() {
        pool.shutdown();
        pool.shutdownNow();
    }

    @Override
    public void shutdownGracefully() {

    }
}
