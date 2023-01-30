package bg.sofia.uni.fmi.mjt.grep.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ConcurrentUtils {

    public static void stop(ExecutorService executor) {
        final int waitTimeMinute = 1;
        try {
            executor.shutdown();
            System.out.print("Waiting for the threads to finish...");
            executor.awaitTermination(waitTimeMinute, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            System.err.println("Termination of thread interrupted");
            e.printStackTrace();
        } finally {
            if (!executor.isTerminated()) {
                System.err.println("Terminating non-finished tasks");
            }
            else {
                System.out.println("Done");
            }

            executor.shutdownNow();
        }
    }

}
