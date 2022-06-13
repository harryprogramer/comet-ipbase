package io.lagpixel.comet.worker;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class SimpleCallableTask extends SimpleTask<Void> {
    public SimpleCallableTask(Runnable r) {
        this(r, 0, TimeUnit.MILLISECONDS);
    }

    public SimpleCallableTask(Runnable r, long delay, TimeUnit unit){
        super(() -> {
            r.run();
            return null;
        }, delay, unit);
    }
}
