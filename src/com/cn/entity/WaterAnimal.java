package com.cn.entity;

public interface WaterAnimal {
    void swim();
    default void breathInWater(){
        System.out.println("breath in water");
    }
}
