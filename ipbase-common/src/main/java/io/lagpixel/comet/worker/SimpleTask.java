package io.lagpixel.comet.worker;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class SimpleTask<T> extends Task<T> {
    private final Callable<T> callable;

    public SimpleTask(Callable<T> callable){
        this(callable, 0, TimeUnit.MILLISECONDS);
    }

    public SimpleTask(Runnable callable, long delay, TimeUnit unit){
        super(delay, unit);
        this.callable = () -> {
            callable.run();
            return null;
        };
    }

    public SimpleTask(Runnable callable){
        this(callable, 0, TimeUnit.MILLISECONDS);
    }

    public SimpleTask(Callable<T> callable, long delay, TimeUnit unit){
        super(delay, unit);
        this.callable = callable;
    }

    @Override
    public T execute(JobWorker worker) throws Throwable {
        return callable.call();
    }

    @Override
    public void cancelled(JobWorker worker, CancellationSource source) {}
}
