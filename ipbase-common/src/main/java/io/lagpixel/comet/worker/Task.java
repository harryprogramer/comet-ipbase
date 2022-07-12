package io.lagpixel.comet.worker;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Task<T> {
    private static final AtomicInteger ids = new AtomicInteger(0);
    public final TimeUnit unit;
    public final long delay;

    private final int id;

    public Task(){
        this(0);
    }

    public Task(long delay){
        this(delay, TimeUnit.MILLISECONDS);
    }

    public Task(long delay, TimeUnit unit){
        this.delay = delay;
        this.unit = unit;
        this.id = ids.getAndIncrement();
    }

    public abstract T execute(JobWorker worker) throws Throwable;

    /**
     * Called when job was cancelled
     * @param worker
     * @param source
     */
    public void cancelled(JobWorker worker, CancellationSource source){}

    public int getID(){
        return id;
    }

    public void cancelJob(@NotNull JobWorker worker, CancellationSource source){
        worker.cancelJob(this, source);
    }

    @Override
    public String toString() {
        return "Task{" +
                "unit=" + unit +
                ", delay=" + delay +
                ", id=" + id +
                '}';
    }
}
