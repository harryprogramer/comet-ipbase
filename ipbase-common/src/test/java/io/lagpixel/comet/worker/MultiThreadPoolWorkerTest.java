package io.lagpixel.comet.worker;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

class MultiThreadPoolWorkerTest {
    static class ExamplePeriodTask extends RepeatedTask {

        public ExamplePeriodTask() {
            super(0, 1, TimeUnit.NANOSECONDS);
        }

        long lastTime = System.currentTimeMillis();
        private long counter = 0;
        @Override
        public void periodTick(JobWorker worker) throws Throwable {
            counter++;

            if((System.currentTimeMillis() - lastTime) > 1000.0){
                System.out.println(counter);
                lastTime = System.currentTimeMillis();
            }
            //System.out.println("siema");
        }
    }

    @Test
    void test() throws InterruptedException {
        JobWorker worker = new ThreadPoolWorker();
        worker.schedulePeriodic(new ExamplePeriodTask());
        worker.schedule(new SimpleTask<>(() -> System.out.println("test"), 2, TimeUnit.SECONDS));
    }

    public static void main(String[] args) throws InterruptedException {
        //MultiThreadPoolWorkerTest thread = new MultiThreadPoolWorkerTest();
        Thread.sleep(2000);
        ThreadPoolWorker poolWorker = new ThreadPoolWorker();
        long start = System.currentTimeMillis();
        SimpleTask<Void> task = new SimpleTask<>(() -> {
            long end = System.currentTimeMillis();
            System.out.println((end -  start) / 1000.0);
        }, 0, TimeUnit.NANOSECONDS);
        poolWorker.schedule(task);

    }
}