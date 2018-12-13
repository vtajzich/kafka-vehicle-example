package com.kafka.vehicle.kafka;

import java.util.concurrent.CountDownLatch;

public class ShutdownHook {

    final CountDownLatch latch = new CountDownLatch(1);
    private final Runnable action;

    ShutdownHook(Runnable action) {
        this.action = action;
    }
    
    public static ShutdownHook of(Runnable action) {
        return new ShutdownHook(action);
    }
    
    public void await() {

        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                latch.countDown();
            }
        });

        try {
            action.run();
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);
    }
}
