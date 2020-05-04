package com.dueclassic.concurrency.utils.test;

import com.dueclassic.concurrency.utils.MyLRUCache;

public class MyLRUCacheTest {
    public static void main(String[] args) throws InterruptedException {
        MyLRUCache<Integer,String> myLruCache = new MyLRUCache<>(3);
        myLruCache.put(1,"Java");
        myLruCache.put(2,"C++");
        myLruCache.put(3,"Python",1000);
        System.out.println(myLruCache.size());//3
        Thread.sleep(2000);
        System.out.println(myLruCache.size());//2
        myLruCache.put(4,"GO");
        System.out.println(myLruCache.size());
        System.out.println(myLruCache.get(1));
    }
}
