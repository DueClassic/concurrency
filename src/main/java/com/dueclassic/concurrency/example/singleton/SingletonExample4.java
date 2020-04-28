package com.dueclassic.concurrency.example.singleton;

import com.dueclassic.concurrency.annoations.NotRecommend;
import com.dueclassic.concurrency.annoations.NotThreadSafe;
import com.dueclassic.concurrency.annoations.ThreadSafe;

/**
 * 懒汉模式 -》双重同步锁单例模式
 * 单例实例在第一次使用时进行创建
 */
@NotThreadSafe
public class SingletonExample4 {

    //私有构造函数
    private SingletonExample4(){
    }

    //单例对象
    private static SingletonExample4 instance=null;

    //静态的工厂方法
    public static SingletonExample4 getInstance(){
        //双重检测机制
        if (instance==null){
            //同步锁
            synchronized (SingletonExample4.class){
                if (instance==null){
                    instance=new SingletonExample4();
                    // 1.memory=allocate() 分配对象的内存空间
                    // 2.ctorInstance() 初始化对象
                    // 3.instance=memory 设置instance指向刚分配的内存

                    //JVM和CPU优化，发生指令重排
                }
            }
        }
        return instance;
    }
}
