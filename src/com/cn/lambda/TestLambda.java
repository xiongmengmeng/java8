package com.cn.lambda;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class TestLambda {
    @Test
    public void test1(){
        Integer[] aa=new Integer[]{2,3,8,6,3};
        Collections.sort(Arrays.asList(aa), new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o1, o2);
            }
        });
    }
    @Test
    public void test2(){
        Integer[] aa=new Integer[]{2,3,8,6,3};
        Collections.sort(Arrays.asList(aa), (o1, o2) ->Integer.compare(o1, o2));
    }
    @Test
    public void test3(){
        Integer[] aa=new Integer[]{2,3,8,6,3};
        Collections.sort(Arrays.asList(aa), Integer::compare);
    }

}
