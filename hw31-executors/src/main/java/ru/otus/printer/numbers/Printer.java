package ru.otus.printer.numbers;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Printer extends Thread {
    private static int slaveThreadId = 2;
    private final int threadId;
    private int counter;
    private final int maxCount;
    private final Object monitor;

    public Printer(int threadId, int counter, int maxCount, Object monitor) {
        this.threadId = threadId;
        this.counter = counter;
        this.maxCount = maxCount;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        for (int i = 1; i < maxCount * 2; ++i) {
            print(i);
        }
    }

    private void print(int maxCount) {
        try {
            synchronized (monitor) {
                while (threadId == slaveThreadId) {
                    monitor.wait();
                }
                System.out.println(String.format("Поток %s: %s", threadId, counter));
                slaveThreadId = threadId;
                counter = maxCount < this.maxCount ? counter + 1 : counter - 1;
                monitor.notifyAll();
            }
        } catch (Exception e) {
            log.error("Ошибка ", e);
            e.printStackTrace();
        }
    }
}
