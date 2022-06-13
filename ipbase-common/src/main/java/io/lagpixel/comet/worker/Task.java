package io.lagpixel.comet.worker;

import java.util.concurrent.TimeUnit;

public abstract class Task<T> {
    public final TimeUnit unit;
    public final long delay;

    public Task(){
        this(0);
    }

    public Task(long delay){
        this(delay, TimeUnit.MILLISECONDS);
    }

    public Task(long delay, TimeUnit unit){
        this.delay = delay;
        this.unit = unit;
    }

    public abstract T execute(JobWorker worker) throws Throwable;

    public void cancelled(JobWorker worker, CancellationSource source){}
}
