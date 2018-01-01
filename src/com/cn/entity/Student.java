package com.cn.entity;

public class Student {
    private String name;
    private int age;
    private int score;
    private Status status;

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", score=" + score +
                ", status=" + status +
                '}';
    }

    public Student(String name, int age, int score, Status status) {
        this.name = name;
        this.age = age;
        this.score = score;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (age != student.age) return false;
        if (score != student.score) return false;
        if (name != null ? !name.equals(student.name) : student.name != null) return false;
        return status == student.status;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + age;
        result = 31 * result + score;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getScore() {
        return score;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setScore(int score) {
        this.score = score;
    }
    public enum Status{
        Free,Busy,Vocation
    }
}
