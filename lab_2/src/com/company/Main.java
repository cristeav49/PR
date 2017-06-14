package com.company;

        import java.util.Random;
        import java.util.concurrent.Semaphore;

public class Main {
    private static final Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore1 = new Semaphore(0);
        Semaphore semaphore2 = new Semaphore(0);
        Semaphore semaphore3 = new Semaphore(0);
        Semaphore semaphore4 = new Semaphore(0);
        Semaphore semaphore5 = new Semaphore(0);

        new Thread(new Runnable() {
            @Override
            public void run() {
                workAndRelease("1",semaphore1); // because is first thread, which doesn't depend on another thread
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                waitWorkAndRelease("2",semaphore1,semaphore2); // thread 2 should wait for working of first thread, and then begin it's task
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                waitWorkAndRelease("3",semaphore2,semaphore3);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                waitWorkAndRelease("4",semaphore3,semaphore4);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                waitWorkAndRelease("5",semaphore4,semaphore5);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                waitWorkAndRelease("6",semaphore5,semaphore5);
            }
        }).start();
    }

    public static void waitWorkAndRelease(String id,Semaphore semaphoreToAquire,Semaphore semaphoreToRelease){
        // gets thread id, semaphore from thread which should be waited, semaphore which will release thsr thread
        try {
            semaphoreToAquire.acquire();
            workAndRelease(id,semaphoreToRelease);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void workAndRelease(String id, Semaphore semaphoreToRelease){ // gets thread id, and semaphore which will be released
        int millis = random.nextInt(2000);
        try {
            Thread.sleep(millis); // sleep for time < 2000
            System.out.printf("Work in %s finished in %d\n",id,millis);
            semaphoreToRelease.release(); // semaphore releases next thread
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
