package com.dueclassic.concurrency.utils;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 使用ConcurrentHashMap + ConcurrentLinkedQueue + ReadWriteLock
 * 实现线程安全的LRUCache
 * @author cc
 * @param <K>
 * @param <V>
 */
public class MyLRUCache<K,V> {
    //最大容量
    private final int maxCapacity;

    //存储数据
    private ConcurrentHashMap<K,V> map;

    //存储键值顺序
    private ConcurrentLinkedQueue<K> keys;

    private ReadWriteLock readWriteLock=new ReentrantReadWriteLock();
    private Lock readLock=readWriteLock.readLock();
    private Lock writeLock=readWriteLock.writeLock();

    //计时器
    private ScheduledExecutorService scheduledExecutorService;

    public MyLRUCache(int maxCapacity){
        if (maxCapacity<0){
            throw new IllegalArgumentException("Illegal max capacity:"+maxCapacity);
        }
        this.maxCapacity=maxCapacity;
        this.map=new ConcurrentHashMap<>(maxCapacity);
        this.keys=new ConcurrentLinkedQueue<>();
        this.scheduledExecutorService= Executors.newScheduledThreadPool(maxCapacity);
    }

    public V put(K key,V value){
        writeLock.lock();
        try {
            if (map.containsKey(key)){
                moveToTailQueue(key);
                map.put(key,value);
                return value;
            }
            if (map.size()==maxCapacity){
                System.out.println("maxCapacity of cache reached");
                removeOldestKey();
            }
            keys.add(key);
            map.put(key,value);
            return value;
        }finally {
            writeLock.unlock();
        }
    }

    public V put(K key,V value,long expireTime) {
        writeLock.lock();
        try {
            if(map.containsKey(key)) {
                moveToTailQueue(key);
                map.put(key, value);
                return value;
            }
            if(map.size()==maxCapacity) {
                System.out.println("maxCapacity of cache reached");
                removeOldestKey();
            }
            keys.add(key);
            map.put(key, value);
            if (expireTime>0) {
                removeAfterExpireTime(key, expireTime);
            }
            return value;
        }finally {
            writeLock.unlock();
        }
    }

    public V get(K key){
        readLock.lock();
        try {
            if (map.containsKey(key)){
                moveToTailQueue(key);
                return map.get(key);
            }
            return null;
        }finally {
            readLock.unlock();
        }
    }

    public V remove(K key){
        writeLock.lock();
        try {
            if (map.containsKey(key)){
                keys.remove(key);
                return map.remove(key);
            }
            return null;
        }finally {
            writeLock.unlock();
        }
    }

    public int size(){
        return map.size();
    }

    //当存储空间已满时，删除最近最少使用的数据
    private void removeOldestKey() {
        K oldestKey=keys.poll();
        if (oldestKey!=null){
            map.remove(oldestKey);
        }
    }

    //将键值移至末尾
    private void moveToTailQueue(K key) {
        keys.remove(key);
        keys.add(key);
    }

    //过期后清除该键值对
    private void removeAfterExpireTime(K key, long expireTime) {
        scheduledExecutorService.schedule(()->{
            map.remove(key);
            keys.remove(key);
        },expireTime,TimeUnit.MILLISECONDS);
    }
}
