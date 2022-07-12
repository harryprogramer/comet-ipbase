package io.lagpixel.comet.worker;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public abstract class VoidTask extends Task<Void> {
    @Contract(pure = true)
    @Override
    public final @Nullable Void execute(JobWorker worker) throws Throwable {
        run(worker);
        return null;
    }

    public abstract void run(JobWorker worker) throws Throwable;
}
