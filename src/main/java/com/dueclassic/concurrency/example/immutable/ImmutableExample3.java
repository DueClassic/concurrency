package com.dueclassic.concurrency.example.immutable;

import com.dueclassic.concurrency.annoations.ThreadSafe;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ThreadSafe
public class ImmutableExample3 {

    private static final ImmutableList<Integer> list=ImmutableList.of(1,2,3);

    private final static ImmutableSet set=ImmutableSet.of(list);

    private final static ImmutableMap<Integer,Integer> map=ImmutableMap.of(1,2,3,4);

    private final static ImmutableMap<Integer,Integer> map1=ImmutableMap.<Integer,Integer>builder()
            .put(1,2)
            .put(3,4)
            .put(5,6)
            .build();

    public static void main(String[] args) {
//        set.add(4);
//        map.put(1,8);
        System.out.println(map.get(1));
    }
}
