package com.dueclassic.concurrency.example.concurrent;

import com.dueclassic.concurrency.annoations.NotThreadSafe;
import com.dueclassic.concurrency.annoations.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@ThreadSafe
public class ConcurrentHashMapExample {
    //请求总数
    public static int clientTotal=5000;
    //同时并发执行的线程数
    public static int threadTotal=200;

    private static Map<Integer,Integer> map=new ConcurrentHashMap<>();

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService= Executors.newCachedThreadPool();
        final Semaphore semaphore=new Semaphore(threadTotal);
        final CountDownLatch countDownLatch=new CountDownLatch(clientTotal);
        for (int i=0;i<clientTotal;i++){
            final int count=i;
            executorService.execute(()->{
                try {
                    semaphore.acquire();
                    update(count);
                    semaphore.release();
                }catch (Exception e){
                    log.error("exception",e);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        log.info("{}",map.size());
    }

    public static void update(int i){
        map.put(i,i);
    }
}
