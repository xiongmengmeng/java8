package com.cn.entity;

public interface LandAnimal {
    void run();
    static void say(){
        System.out.println("interface can use a static method");
    }
    default void breathInLand(){
        System.out.println("breath in air");
    }
}
