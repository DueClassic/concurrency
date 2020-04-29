package com.dueclassic.concurrency.example.sycnContainer;

import com.dueclassic.concurrency.annoations.NotThreadSafe;
import com.dueclassic.concurrency.annoations.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Slf4j
@ThreadSafe
public class VectorExample1 {

    //请求总数
    public static int clientTotal=5000;
    //同时并发执行的线程数
    public static int threadTotal=200;

    private static Vector<Integer> list=new Vector<>();

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService= Executors.newCachedThreadPool();
        final Semaphore semaphore=new Semaphore(threadTotal);
        final CountDownLatch countDownLatch=new CountDownLatch(clientTotal);
        for (int i=0;i<clientTotal;i++){
            executorService.execute(()->{
                try {
                    semaphore.acquire();
                    update();
                    semaphore.release();
                }catch (Exception e){
                    log.error("exception",e);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        log.info("{}",list.size());
    }

    public static void update(){
        list.add(1);
    }
}
