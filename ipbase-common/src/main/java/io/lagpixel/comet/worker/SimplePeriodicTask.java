package io.lagpixel.comet.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SimplePeriodicTask extends RepeatedTask {
    private final static Logger logger = LoggerFactory.getLogger(SimplePeriodicTask.class);
    private final Runnable tickR;
    private final Runnable cancelR;

    public SimplePeriodicTask(Runnable r, Runnable cancelR, long delay, long period, TimeUnit unit) {
        super(delay, period, unit);
        this.tickR = Objects.requireNonNull(r);
        this.cancelR = cancelR;
    }

    public SimplePeriodicTask(Runnable r, long delay, long period, TimeUnit unit) {
        this(r, null, delay, period, unit);

    }

    public SimplePeriodicTask(Runnable r, long delay, long period) {
        this(r, null, delay, period, TimeUnit.MILLISECONDS);
    }

    public SimplePeriodicTask(Runnable r, long period) {
        this(r, null,0, period, TimeUnit.MILLISECONDS);
    }

    @Override
    public void cancelled(JobWorker worker, CancellationSource source) {
        super.cancelled(worker, source);
        logger.info("SimplePeriodicTask.cancelled(): task id {} was cancelled because {}", getID(), source.getReason());
        if(cancelR != null){
            cancelR.run();
        }
    }


    @Override
    public void periodTick(JobWorker worker) throws Throwable {
        tickR.run();
    }
}
