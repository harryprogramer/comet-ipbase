package io.lagpixel.comet.worker;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public final class ThreadCometFactory implements ThreadFactory {
    public static final ThreadCometFactory INSTANCE = new ThreadCometFactory();

    private ThreadCometFactory(){}

    @Override
    public Thread newThread(@NotNull Runnable runnable) {
        return new Thread(runnable);
    }
}
