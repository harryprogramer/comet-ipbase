package io.lagpixel.comet.channel;

import io.lagpixel.comet.worker.Task;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;

public interface FutureJob<T>{
    /**
     * Get task id given from worker.
     * @return task identification id
     */
    int getId();

    /**
     * Check if task was cancelled by worker or user
     * @return is task was canceled
     */
    boolean isCancelled();

    /**
     * Check if task was execution is done. It doesn't matter if task throw any exception.
     * It just tells you if the job has been completed.
     * @return is task done
     */
    boolean isDone();

    /**
     * Check if the task has any errors while execution.
     * @return is task ended success
     */
    boolean isSuccess();

    /**
     *
     * If any error occurs while executing the task,
     * you can check for an exception using this function.
     * Before calling, you should first check if the task ended with
     * an error in this method {@link FutureJob#isSuccess()}
     * @return
     */
    Throwable getCause();

    void cancel(boolean isInterrupt);

    /**
     * Method used to synchronize with task
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    T get() throws ExecutionException, InterruptedException;

    Task<T> getTask();

    ScheduledFuture<T> scheduledFuture();

    default Future<T> future(){
        return scheduledFuture();
    }
}
