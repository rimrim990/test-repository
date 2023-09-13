package learn.java.testrepository.plain.synchronize;

import java.util.concurrent.TimeUnit;

public class SynchronizedStopThread {

    private static boolean stopRequested; // 전역 공유 변수

    private synchronized void requestStop() {
        stopRequested = true;
    }

    private synchronized boolean isStopRequested() {
        return stopRequested;
    }

    public void run() throws InterruptedException {
        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!isStopRequested()) {
                i++;
                System.out.println("Background thread still running with id " + i + " ...");
            }
        });
        backgroundThread.start();

        TimeUnit.SECONDS.sleep(1);
        requestStop();
    }
}
