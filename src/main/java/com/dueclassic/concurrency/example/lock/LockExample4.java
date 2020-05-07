package com.dueclassic.concurrency.example.lock;

import com.dueclassic.concurrency.annoations.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@ThreadSafe
public class LockExample4 {
    public static void main(String[] args) {
        ReentrantLock lock=new ReentrantLock();

        Condition condition=lock.newCondition();

        new Thread(()->{
            try {
                lock.lock();
                log.info("wait signal");
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("get signal");
            lock.unlock();
        }).start();

        new Thread(()->{
            lock.lock();
            log.info("get lock");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            condition.signalAll();
            log.info("send signal");
            lock.unlock();
        }).start();
    }
}
