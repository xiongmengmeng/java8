package com.cn.stream;


import com.cn.entity.Student;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Stream的三个操作步骤
 * 1.创建Stream
 * 2.中间操作
 * 3.终止操作
 */

public class TestStreamApi {
    /**
     * 创建Stream
     */
    @Test
    public void creatStream(){
        //1.通过Collection系列集合提供的stream()或者parallelStream();
        List<String> list=new ArrayList<String>();
        Stream<String> stream1 = list.stream();
        //2.通过Arrays中的静态方法Stream()获取数组流
        Integer[] ints=new Integer[10];
        Stream<Integer> stream2 = Arrays.stream(ints);
        //3.通过Stream类中的静态方法of()
        Stream<String> stream3 = Stream.of("aa", "bb", "cc");
        //4.创建无限流
        //迭代
        Stream<Integer> stream4 = Stream.iterate(0, x -> x + 2);
        //生成
        Stream<Double> stream5 = Stream.generate(() -> Math.random());
    }
    List<Student> students=Arrays.asList(
            new Student("mm",22,78,Student.Status.Busy),
            new Student("kk",23,98,Student.Status.Free),
            new Student("qq",21,56,Student.Status.Vocation),
            new Student("hh",20,43,Student.Status.Busy),
            new Student("hh",20,43,Student.Status.Busy)
    );

    /**
     * 中间操作--筛选和切片
     */
    @Test
    public void middle_(){
        //filer:过滤  Stream<T> filter(Predicate<? super T> predicate);
        System.out.println("-------------------------------------------");
        Stream<Student> filer = students.stream().filter(s -> s.getScore() > 60);
        filer.forEach(System.out::println);
        //limit:截断
        System.out.println("-------------------------------------------");
        Stream<Student> limit = students.stream().limit(2);
        limit.forEach(System.out::println);
        //skip:跳过
        System.out.println("-------------------------------------------");
        Stream<Student> skip = students.stream().skip(2);
        skip.forEach(System.out::println);
        //distinct:去重，通过流所生成元素的hashCode()和equals()去除重复的元素（也就是说Student要重写hashCode()和equals()）
        System.out.println("-------------------------------------------");
        Stream<Student> distinct = students.stream().distinct();
        distinct.forEach(System.out::println);
    }
    /**
     * 中间操作--映射
     */
    @Test
    public void middle_map(){
        //map：映射，参数为Function<? super T, ? extends R> mapper
        System.out.println("-------------------------------------------");
        Stream<String>  map= students.stream().map(Student::getName);
        map.forEach(System.out::println);
        //flatmap:映射,参数为Function<? super T, ? extends Stream<? extends R>> mapper,将Function方法返回的每个流中的每个元素放到流中
        //map与flatmap的区别像是list的add()与addAll()
        System.out.println("-------------------------------------------");
        Stream<Character> flatMap = students.stream().flatMap(s -> toBeCharacter(s.getName()));
        flatMap.forEach(System.out::println);
    }
    public static Stream<Character> toBeCharacter(String str){
        List<Character> list=new ArrayList<>();
        for (Character c:str.toCharArray()){
            list.add(c);
        }
        return list.stream();
    }
    /**
     * 中间操作--排序
     */
    @Test
    public void middle_sort(){
        //sorted():排序,数组中的对象需要实现Comparable
        //sorted(Comparator<? super T> comparator):排序,通过Comparator定制排序
        Stream<Student> sorted = students.stream().sorted((x, y) -> Integer.compare(x.getAge(), y.getAge()));
        sorted.forEach(System.out ::println);
    }
    /**
     * 终止操作--查找与匹配
     */
    @Test
    public void end_match_find(){
        //allMatch:检查是否匹配所有元素
        boolean allMatch = students.stream().allMatch(s -> s.getStatus().equals(Student.Status.Busy));
        System.out.println("是否所有人都很忙："+allMatch);
        //anyMatch:检查是否匹配一个元素
        boolean anyMatch = students.stream().anyMatch(s -> s.getStatus().equals(Student.Status.Busy));
        System.out.println("是否有人很忙："+anyMatch);
        //noneMatch:检查是否没有匹配所有元素
        boolean noneMatch = students.stream().noneMatch(s -> s.getStatus().equals(Student.Status.Busy));
        System.out.println("是否所有人都很闲："+noneMatch);
        //findFirst：返回第一个元素
        Optional<Student> first = students.stream().findFirst();
        System.out.println("第一个元素："+first.get());
        //findAny:返回当前流中的任意元素
        Optional<Student> any = students.parallelStream().filter(s -> s.getStatus().equals(Student.Status.Busy)).findAny();
        System.out.println("任一个元素："+any.get());
    }
    /**
     * 终止操作--统计
     */
    public void end_Statistics(){
        //count：返回流中元素的总个数
        long count = students.stream().count();
        System.out.println("学生人数："+count);
        //max:返回流中最大值
        Optional<Student> max = students.stream().max((x, y) -> Integer.compare(x.getScore(), y.getScore()));
        System.out.println("分数最高的学生："+max.get());
        //min:返回流中最小值
        Optional<Integer> min = students.stream().map(Student::getScore).min(Integer::compare);
        System.out.println("最低分数"+min.get());
    }

    /**
     * 终止操作--归约，收集
     */
    @Test
    public void end_collection(){
        //reduce:归约
        Integer reduce = students.stream().map(s -> s.getScore()).reduce(0, (x, y) -> x + y);
        System.out.println("分数总和："+reduce);
        //collection:收集，给stream中的元素做汇总，参数Collector<? super T, A, R> collector
        System.out.println("----------------------------list------------------------------");
        List<String> list = students.stream().map(s -> s.getName()).collect(Collectors.toList());
        list.forEach(System.out::println);
        System.out.println("-------------------------set---------------------------------");
        Set<String> set = students.stream().map(s -> s.getName()).collect(Collectors.toSet());
        set.forEach(System.out::println);
        System.out.println("------------------------hashSet----------------------------------");
        HashSet<String> hashSet = students.stream().map(s -> s.getName()).collect(Collectors.toCollection(HashSet::new));
        hashSet.forEach(System.out::println);
        //平均值
        Double average = students.stream().collect(Collectors.averagingInt(s -> s.getScore()));
        System.out.println("平均分："+average);
        //总和
        Integer sum = students.stream().collect(Collectors.summingInt(s -> s.getScore()));
        System.out.println("总分："+sum);
        //分组
        Map<Student.Status, List<Student>> group = students.stream().collect(Collectors.groupingBy(Student::getStatus));
        System.out.println("----------------------------group by------------------------------");
        for (Student.Status s:group.keySet()){
            System.out.println(s+":"+group.get(s));
        }
    }
}
