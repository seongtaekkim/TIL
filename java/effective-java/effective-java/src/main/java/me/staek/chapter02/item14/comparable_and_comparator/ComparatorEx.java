package me.staek.chapter02.item14.comparable_and_comparator;

import java.util.*;

/* class name switch test */
class Student implements Comparable<Student> {
//class Student {
    String name;
    int id;
    public Student(String name, int id){
        this.name = name;
        this.id = id;
    }
    public String toString() {
        return "이름: "+name+", 식별번호: "+id;
    }
    /* 기본 정렬 기준: 오름차순 */
    @Override
    public int compareTo(Student anotherStudent) {
        return Integer.compare(id, anotherStudent.id);
    }
}

/* class name switch test */
class Processor <E extends Comparable<E>>{
//class Processor <E> {
    E[] o;
    public Processor(E[] o) {
        this.o = o;
    }
}

/**
 * TODO Processor가 Arrays.sort 등의 Comparable 구현체가 필요한 메서드를 사용해야 한다면,
 *      <E> 가 아니라 <E extends Comparable<E>> 를 작성해야 한다.
 *      -> 제네릭 타입이 Comparable 구현 클래스가 아닌경우, 컴파일에러가 발생한다.
 *      -> <E> 를 작성했다면, ClassCastException 라는 RuntimeException 이 발생한다.
 */
public class ComparatorEx {
    public static void main(String[] args) {
        Student student[] = new Student[5];

        student[0] = new Student("DDDD", 20120001);
        student[1] = new Student("AAAA", 20150001);
        student[2] = new Student("EEEE", 20110001);
        student[3] = new Student("BBBB", 20130001);
        student[4] = new Student("CCCC", 20140001);
        Arrays.sort(student);

        // 이름순서로 조회
//        Arrays.sort(student, new java.util.Comparator<Student>() {
//            @Override
//            public int compare(Student o1, Student o2) {
//                if (o1.name.compareTo(o2.name) > 0)
//                    return (1);
//                return (-1);
//            }
//        });
        for(int i=0 ; i<student.length ; i++)
            System.out.println(student[i]);
        System.out.println("=======================================");


        Processor<Student> p = new Processor<Student>(student);
//        Arrays.sort(p.o);
        Arrays.sort(p.o, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                if (o1.name.compareTo(o2.name) > 0)
                    return (1);
                return (-1);
            }
        });
        for(int i=0 ; i<p.o.length ; i++)
            System.out.println(student[i]);




        /**
         *
         *   public static <T extends Comparable<? super T>> void sort(List<T> list) {
         *         list.sort(null);
         *   }
         * TODO API예제) Collections.sort(..);
         *      Collections 의 sort 제네릭 메서드를 사용하기 위해선
         *      List의 타입은 Comparable 구현체여야 한다.
         *      그렇지 않으면 컴파일에러가 발생한다.
         */
        List<Student2> list = new ArrayList<>();
        list.add(new Student2("kim", 20120001));
        Collections.sort(list);

    }
}
