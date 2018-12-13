package com.kafka.vehicle.kafka;

import java.util.concurrent.CountDownLatch;

public class ShutdownHook {

    final CountDownLatch latch = new CountDownLatch(1);
    private final Runnable shutdownAction;
    private final Runnable action;

    ShutdownHook(Runnable action, Runnable shutdownAction) {
        this.action = action;
        this.shutdownAction = shutdownAction;
    }
    
    public static ShutdownHook of(Runnable action, Runnable shutdownAction) {
        return new ShutdownHook(action, shutdownAction);
    }
    
    public void await() {

        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                
                shutdownAction.run();
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
