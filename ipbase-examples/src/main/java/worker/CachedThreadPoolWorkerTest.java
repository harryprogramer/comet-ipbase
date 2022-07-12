package worker;

import io.lagpixel.comet.channel.FutureJob;
import io.lagpixel.comet.worker.ThreadPoolWorker;
import io.lagpixel.comet.worker.SimpleTask;

import java.util.concurrent.ExecutionException;

public class CachedThreadPoolWorkerTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadPoolWorker poolWorker = new ThreadPoolWorker();
        FutureJob<String> future = poolWorker.schedule(new SimpleTask<>(() -> {
            return "siema";
        }));

        while(!future.isDone()){
            System.out.println("still waiting...");
            Thread.sleep(100);
        }

        String result = future.get();

        System.out.println(result);
        System.out.println(future.isSuccess());
        System.out.println(future.getCause());
        poolWorker.shutdown();
    }
}
