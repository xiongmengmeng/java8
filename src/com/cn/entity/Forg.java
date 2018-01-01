package com.cn.entity;

import org.junit.Test;

public class Forg extends Mouth implements WaterAnimal,LandAnimal {

    @Override
    public void run() {

    }

    @Override
    public void swim() {

    }

/*
    @Override
    public void breath() {
       LandAnimal.super.breath();
    }
*/
    @Test
    public  void main(String[] args){
        Forg f=new Forg();
       /* f.breath();*/
        f.breathInLand();
        f.breathInWater();
       /* LandAnimal.say();*/
    }





}
