package com.dueclassic.concurrency.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SemaphoreExample2 {

    private final static int threadCount=20;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec= Executors.newCachedThreadPool();

        final Semaphore semaphore=new Semaphore(3);

        for (int i=0;i<threadCount;i++){
            final int threadNum=i;
            exec.execute(()->{
                try {
                    if (semaphore.tryAcquire(5000,TimeUnit.MILLISECONDS)){
                        test(threadNum);
                        semaphore.release();
                    }
                } catch (Exception e) {
                    log.error("exception",e);
                }
            });
        }
        log.info("finish");
        exec.shutdown();
    }

    public  static void test(int threadNum)throws Exception{
        log.info("{}",threadNum);
        Thread.sleep(1000);
    }
}
