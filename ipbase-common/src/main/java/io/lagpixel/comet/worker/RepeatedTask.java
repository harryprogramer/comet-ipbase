package io.lagpixel.comet.worker;

import java.util.concurrent.TimeUnit;

public abstract class RepeatedTask extends Task<Void> {
    public final long period;

    public RepeatedTask(long delay, long period, TimeUnit unit){
        super(delay, unit);
        this.period = period;
    }

    @Override
    public final Void execute(JobWorker worker) throws Throwable{
        periodTick(worker);
        return null;
    }

    public abstract void periodTick(JobWorker worker) throws Throwable;

    @Override
    public String toString() {
        return "RepeatedTask{" +
                "period=" + period +
                ", unit=" + unit +
                ", delay=" + delay +
                '}';
    }
}
