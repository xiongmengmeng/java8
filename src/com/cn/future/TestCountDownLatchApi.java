package com.cn.future;

import org.junit.Test;

import javax.activation.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static java.util.stream.Collectors.toList;

public class TestCountDownLatchApi {
    //创建线程池
    ThreadPoolExecutor executor  = new ThreadPoolExecutor(6,6,2, TimeUnit.MINUTES,new LinkedBlockingDeque<Runnable>());

    //任务(耗时就用sleep)
    public Double task(Long start,String name)  {
        double taskResult = Math.random() * 1000;
        try {
            Thread.sleep((long)taskResult);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("执行"+name+",耗时：" + (System.nanoTime() - start) / 1_000_000 + " msecs");
        return taskResult;
    }

    /**
     * 异常调用:使用Callable，返回值用Future接收,使用get()方法阻塞
     * @throws Exception
     */
    @Test
    public void useCallable() throws Exception{
        long start = System.nanoTime();
        List<Double> list=new ArrayList<>();
        Future<Double> future1 = executor.submit(()->task(start,"任务1"));
        Future<Double> future2 = executor.submit(()->task(start,"任务2"));
        list.add(future1.get());
        list.add(future2.get());
        System.out.println("全部计算完成,耗时："+ (System.nanoTime() - start) / 1_000_000 + " msecs");
        System.out.println("list:"+ list);
    }


    public void task(Long start,String name,List<Double> list,CountDownLatch countDownLatch)  {
        double taskResult = Math.random() * 1000;
        try {
            Thread.sleep((long)taskResult);
            list.add(taskResult);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            countDownLatch.countDown();
        }
        System.out.println("执行"+name+",耗时：" + (System.nanoTime() - start) / 1_000_000 + " msecs");
    }

    /**
     * 异常调用，使用CountDownLatch,await()方法来阻塞
     * @throws Exception
     */
    @Test
    public void useCountDownLatch() throws Exception{
        long start = System.nanoTime();
        List<Double> list=new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(2);
        executor.submit(()->task(start,"任务1",list,countDownLatch));
        executor.submit(()->task(start,"任务2",list,countDownLatch));
        countDownLatch.await();
        System.out.println("全部计算完成,耗时："+ (System.nanoTime() - start) / 1_000_000 + " msecs");
        System.out.println("list:"+ list);
    }



}
