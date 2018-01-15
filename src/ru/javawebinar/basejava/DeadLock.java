package ru.javawebinar.basejava;

public class DeadLock {
    private static final String LOCK1 = "firstLock";
    private static final String LOCK2 = "secondLock";

    private static void threadOut(String info) {
        System.out.println(Thread.currentThread().getName() + ": " + info);
    }

    private static void doSomeWorkWithLocks(String lock1, String lock2) {
        threadOut("Trying to gather lock (" + lock1 + ")");
        synchronized (lock1) {
            threadOut("Got lock (" + lock1 + ")");
            try {
                threadOut("Do some work...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            threadOut("Work done. Trying to gather lock(" + lock2 + ")");
            synchronized (lock2) {
                threadOut("Got lock (" + lock2 + ")");
            }
            threadOut("Release lock(" + lock2 + ")");
        }
        threadOut("Release lock(" + lock1 + ")");
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> DeadLock.doSomeWorkWithLocks(LOCK1, LOCK2));
        Thread t2 = new Thread(() -> DeadLock.doSomeWorkWithLocks(LOCK2, LOCK1));

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Work is done.");
    }
}
