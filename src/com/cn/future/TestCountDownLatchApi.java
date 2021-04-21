package com.cn.future;

import org.junit.Test;

import javax.activation.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class TestCountDownLatchApi {
    //创建线程池
    ThreadPoolExecutor executor  = new ThreadPoolExecutor(20,20,2, TimeUnit.MINUTES,new LinkedBlockingDeque<Runnable>());


    List<String> taskList = Arrays.asList("任务1",
            "任务2",
            "任务3",
            "任务4",
            "任务5",
            "任务6");

    //任务
    public Long task(Long start,String name)  {
        //Math.random() *
        long sum=0;
        for (long i = 0; i <1000000000L ; i++) {
            sum=sum+i;
        }
        //System.out.println("执行"+name+",耗时：" + (System.nanoTime() - start) / 1_000_000 + " msecs");
        return sum;
    }
    //任务,主要是为了执行countDownLatch.countDown();
    public Long task(Long start,String name,CountDownLatch countDownLatch)  {
        //Math.random() *
        long sum=0;
        for (long i = 0; i <1000000000L ; i++) {
            sum=sum+i;
        }
        countDownLatch.countDown();
        //System.out.println("执行"+name+",耗时：" + (System.nanoTime() - start) / 1_000_000 + " msecs");
        return sum;
    }

    /**
     * 异常调用:使用Callable，返回值用Future接收,使用get()方法阻塞
     * @throws Exception
     */
    public void useCallable() throws Exception{
        long start = System.nanoTime();
        List<Long> list=new ArrayList<>();
        List<Future<Long>> futureList = taskList
                .stream()
                .map(o -> executor.submit(() -> task(start, o)))
                .collect(Collectors.toList());
        for (Future<Long> f: futureList) {
            list.add(f.get());
        }
        System.out.println("使用Future,耗时："+ (System.nanoTime() - start) / 1_000_000 + " msecs");
        //System.out.println("list："+ list);
    }




    /**
     * 异常调用，使用CountDownLatch,await()方法来阻塞
     * @throws Exception
     */
    public void useCountDownLatch() throws Exception{
        long start = System.nanoTime();
        List<Long> list=new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(6);
        List<Future<Long>> futureList = taskList
                .stream()
                .map(o ->executor.submit(()->task(start,o,countDownLatch)))
                .collect(Collectors.toList());
        countDownLatch.await();
        for (Future<Long> f: futureList) {
            list.add(f.get());
        }
        System.out.println("使用CountDownLatch,耗时："+ (System.nanoTime() - start) / 1_000_000 + " msecs");
        //System.out.println("list："+ list);
    }



    /**
     * 异常调用，使用CompletableFuture,join()方法来阻塞
     * @throws Exception
     */
    public void useCompletableFuture() throws Exception{
        long start = System.nanoTime();
        List<Long> list=new ArrayList<>();
        List<CompletableFuture<Long>> completableFutureList = taskList.stream()
                .map(o -> CompletableFuture.supplyAsync(() -> task(start, o), executor))
                .collect(Collectors.toList());

        CompletableFuture<Void> allOf = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[0]));
        allOf.join();
        for (CompletableFuture<Long> c: completableFutureList) {
            list.add(c.get());
        }
        System.out.println("使用CompletableFuture,耗时："+ (System.nanoTime() - start) / 1_000_000 + " msecs");
        //System.out.println("list："+ list);
    }

    @Test
    public void test() throws Exception {
        useCallable();
        useCountDownLatch();
        useCompletableFuture();
    }

}
