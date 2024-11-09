package io.jenkins.plugins;

import java.util.concurrent.CountDownLatch;

public class CompletionLatch {
    private static CompletionLatch instance;
    private CountDownLatch latch;

    private CompletionLatch() {
        latch = new CountDownLatch(1);
    }

    public static synchronized CompletionLatch getInstance() {
        if (instance == null) {
            instance = new CompletionLatch();
        }
        return instance;
    }

    public void await() throws InterruptedException {
        latch.await();
    }

    public void countDown() {
        latch.countDown();
    }

    public void reset() {
        latch = new CountDownLatch(1);
    }
}
