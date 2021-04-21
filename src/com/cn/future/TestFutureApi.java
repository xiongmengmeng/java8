package com.cn.future;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static java.util.stream.Collectors.toList;

public class TestFutureApi {
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

    @Test
    public void testFuture() throws Exception{
        long start = System.nanoTime();
        //1.向线程池提交异步任务1,使用future来接收线程结果
        Future<Double> future = executor.submit(()->task(start,"任务1"));
        //2.主流程执行任务2
        Double task2Result = task(start,"任务2");
        //3.future.get(),阻塞操作，直到获取异步操作的结果
        Double task1Result = future.get();

        System.out.println("全部计算完成,耗时："+ (System.nanoTime() - start) / 1_000_000 + " msecs");
        System.out.println("task1Result:"+ task1Result);
        System.out.println("task2Result:"+ task2Result);
    }


    @Test
    public void testCompletableFuture() throws InterruptedException, ExecutionException {
        long start = System.nanoTime();
        //1.向线程池提交异步任务1,使用CompletableFuture来接收线程结果
        CompletableFuture<Double> task2 = CompletableFuture.supplyAsync(() -> task(start, "任务1"),executor);
        //2.主流程执行任务2
        Double task1Result = task(start, "任务2");
        //3.future.get(),阻塞操作，直到获取异步操作的结果
        Double task2Result = task2.get();
        System.out.println("全部计算完成,耗时："+ (System.nanoTime() - start) / 1_000_000 + " msecs");
        System.out.println("task1Result:"+ task1Result);
        System.out.println("task2Result:"+task2Result );
    }


    List<String> taskList = Arrays.asList("任务1",
            "任务2",
            "任务3",
            "任务4",
            "任务5",
            "任务6");



    //任务(耗时就用sleep)
    public Long taskUpgrade(Long start,String name)  {
        long sum=0;
        for (long i = 0; i <1000000000L ; i++) {
            sum=sum+i;
        }
        //System.out.println("执行"+name+",耗时：" + (System.nanoTime() - start) / 1_000_000 + " msecs");
        return sum;
    }
    /**
     * 使用流顺序计算
     * @param start
     * @return
     */
    public List<Long> getTaskResult(long start) {
        return taskList.stream()
                .map(taskName -> taskUpgrade(start,taskName))
                .collect(toList());
    }

    /**
     * 使用流并行计算
     * @param start
     * @return
     */
    public List<Long> getTaskResultParallel(long start) {
        return taskList.parallelStream()
                .map(taskName -> taskUpgrade(start,taskName))
                .collect(toList());
    }

    /**
     * 异步运算:使用默认执行器
     * @param start
     * @return
     */
    public List<Long> getTaskResultFuture(long start) {
        List<CompletableFuture<Long>> futureList = taskList.stream()
                .map(taskName -> CompletableFuture.supplyAsync(() -> taskUpgrade(start,taskName)))
                .collect(toList());
        //CompletableFuture 类中的 join 方法和 Future 接口中的 get 有相同的含义
        return futureList.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }

    /**
     * 异步运算:使用定制的执行器
     * @param start
     * @return
     */
    public List<Long> getTaskResultFuture1(long start) {
        List<CompletableFuture<Long>> futureList = taskList.stream()
                .map(taskName -> CompletableFuture.supplyAsync(() -> taskUpgrade(start,taskName), executor))
                .collect(toList());
        //CompletableFuture 类中的 join 方法和 Future 接口中的 get 有相同的含义
        return futureList.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }

    @Test
    public void test3() {
        long start = System.nanoTime();
        getTaskResult(start);
        System.out.println("使用流顺序计算：" + (System.nanoTime() - start) / 1_000_000 + " msecs");
        start = System.nanoTime();
        getTaskResultParallel(start);
        System.out.println("使用流【并行】计算：" + (System.nanoTime() - start) / 1_000_000 + " msecs");
        start = System.nanoTime();
        getTaskResultFuture(start);
        System.out.println("异步运算:使用【默认执行器】,【并发】：" + (System.nanoTime() - start) / 1_000_000 + " msecs");

        //并行和并发不相伯仲，究其原因都一样：它们内部采用的是同样的通用线程池，默认都使用固定数目的线程，具体线程数取决于
        //Runtime.getRuntime().availableProcessors() 的返回值。
        //然而，CompletableFuture 具有一定的优势，因为它允许你对执行器（ Executor ）进行配置，尤其是线程池的大小
        start = System.nanoTime();
        getTaskResultFuture1(start);
        System.out.println("异步运算:使用【定制的执行器】，【并发】：" + (System.nanoTime() - start) / 1_000_000 + " msecs");

    }


}
