package me.staek.chapter02.item14.comparable_and_comparator;

import java.util.Arrays;


class Person {
    protected int id;
    public int getId() {
        return id;
    }
    public Person(int id) {
        this.id = id;
    }
}

/* class name switch test */
// class Student2 extends Person implements Comparable<Student2> {
class Student2 extends Person implements Comparable<Person> {
    public String name;
    public Student2(String name, int id){
        super(id);
        this.name = name;
    }
    public String toString(){
        return "이름: "+name+", 식별번호: "+id;
    }

     @Override
     public int compareTo(Person o) {
         return Integer.compare(id, o.getId());
     }

//     @Override
//     public int compareTo(Student2 o) {
//         return Integer.compare(id, o.getId());
//     }
 }

/* class name switch test */
//class Professor2 <E extends Comparable<E>>{
 class Professor2<E extends Comparable<? super E>>{
    E[] s;
    public Professor2(E[] s) {
        this.s = s;
    }
}


/**
 * TODO Processor2가 <E extends Comparable<E>> 를 작성해서 컴파일에러를 피하고 작성해도 안전하지 않다.
 *      제네릭 타입인 Student2가 Person을 상속할때, Comparable이 Student2의 필드인 경우는 괜찮다.
 *      하지만 compareTo가 Person의 필드를인자로 받아 구현한다면,<E extends Comparable<E>> 의 E는 Student2 이지만 Person이 될 수 없으므로 컴파일에러가 발생한다.
 *      <<해결방법>> : <E extends Comparable<E>>를 <E extends Comparable<? super E>> 로 변경한다.
 *                   ?는 E를 포함한 부모를 의미하므로 비로서 안전한 코드가 된다.
 */
public class ComparatorEx2 {
    public static void main(String[] args) {
        Student2 student[] = new Student2[5];
        //순서대로 "이름", 학번, 학점
        student[0] = new Student2("DDDD", 20120001);
        student[1] = new Student2("AAAA", 20150001);
        student[2] = new Student2("EEEE", 20110001);
        student[3] = new Student2("BBBB", 20130001);
        student[4] = new Student2("CCCC", 20140001);





        //Arrays.sort(student);
        // 이름순서로 조회
//        Arrays.sort(student, new java.util.Comparator<Student2>() {
//            @Override
//            public int compare(Student2 o1, Student2 o2) {
//                if (o1.name.compareTo(o2.name) > 0)
//                    return (1);
//                return (-1);
//            }
//        });
        for(int i=0;i<5;i++)
            System.out.println(student[i]);


        Professor2<Student2> s2 = new Professor2<Student2>(student);
        Arrays.sort(s2.s);// 오름차순
        Arrays.sort(s2.s, new java.util.Comparator<Student2>() {
            @Override
            public int compare(Student2 o1, Student2 o2) {
                return o1.compareTo(o2);
            }
        });

        System.out.println("==========================");
//        for(int i=0;i<5;i++)
//            System.out.println(s2.s[i]);
    }
}
