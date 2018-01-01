package com.cn.lambda;

import java.util.LinkedList;
import java.util.List;

public class MyCreatorTest {

    public static <T> List<T> asList(MyCreator<List<T>> mc, T... a) {
        List<T> list=mc.Create();
        for(T t:a){
            list.add(t);
        }
        return list;
    }
    public void test2(){
        List<Integer> li=this.asList(LinkedList::new,2,7,5,4);
        System.out.println(li.getClass());
    }

    public void test3(){
        List<Integer> li=this.asList(()->new LinkedList(),2,7,5,4);
        System.out.println(li.getClass());
    }
    public void test4(){
        List<Integer> li=this.asList(new MyCreator<List<Integer>>() {
            @Override
            public List<Integer> Create() {
                return new LinkedList();
            }
        }, 2, 7, 5, 4);
        System.out.println(li.getClass());
    }
}
