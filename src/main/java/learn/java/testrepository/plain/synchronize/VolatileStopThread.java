package learn.java.testrepository.plain.synchronize;

import java.util.concurrent.TimeUnit;

public class VolatileStopThread {

    private static volatile boolean stopRequested; // 전역 공유 변수

    public void run() throws InterruptedException {
        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!stopRequested) {
                i++;
                System.out.println("Background thread still running with id " + i + " ...");
            }
        });
        backgroundThread.start();

        TimeUnit.SECONDS.sleep(1);
        stopRequested = true;
    }
}
