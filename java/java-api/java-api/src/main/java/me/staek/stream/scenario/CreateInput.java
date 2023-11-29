package me.staek.stream.scenario;

import me.staek.stream.vo.User;

import java.util.*;
import java.util.stream.*;

public class CreateInput {

    private List<User> users;

    private boolean hasUser() {
        return users.stream().anyMatch(f -> f.getName().contains("kim"));
    }

    public static void main(String[] args) {
        CreateInput input = new CreateInput();

        String[] arr = new String[] {"apple", "banana"};

        Stream<String> arr1 = Arrays.stream(arr);
        Stream<String> arr2 = Stream.of(arr);

        arr1.forEach(System.out::println);
        arr2.forEach(System.out::println);

        IntStream ints = new SplittableRandom().ints();
        LongStream longs = new SplittableRandom().longs();
        DoubleStream doubles = new SplittableRandom().doubles();

        IntStream range1 = IntStream.range(1, 3); // ()
        IntStream range2 = IntStream.rangeClosed(1, 3); // (]

//        range1.forEach(System.out::println);
//        range2.forEach(System.out::println);

        Stream.iterate(100, n -> n+1)
                .limit(3)
                .forEach(System.out::println);

        IntStream.iterate(100, n->n+2)
                        .limit(3)
                                .forEach(System.out::println);
        /*
        IntStream.generate(new IntSupplier() {
            @Override
            public int getAsInt() {
                return 1;
            }
        }).limit(10).mapToObj().collect(Collectors.toList());

         */

        List<User> users = new ArrayList<>();
        users.add(new User("1", "kim seong taek", "1234", 30));
        users.add(new User("1", "kim seong taek", "1234", 31));
        users.add(new User("2", "kim seong taek", "1234", 32));
        // distinct : equels && hashcode 기준
        System.out.println(users.stream().distinct().count());

        users.stream().filter(f -> f.getId().equals("1"))
                .findFirst()
                .orElseThrow(NoSuchFieldError::new);



        String[] fruits = new String[] {"apple", "banana"};

        Stream<String> fruits2 = Stream.of(arr);
        System.out.println(fruits2.sorted().collect(Collectors.joining(" ")));;
//        fruits2.forEachOrdered();


        // 맵을 만들 때 중복 키가 존재할 시 런타임에러가 발생할 수 있다.
        // runtime error
//        Map<String, User> collect = users.stream().collect(Collectors.toMap(s -> s.getName(), s -> s));

        // 키가 겹치면 먼저들어온 키만 저장하겟다
        Map<String, User> collect =
                users.stream().collect(
                        Collectors.toMap(s -> s.getName(), s -> s, (exist, replace) -> exist));
        System.out.println(collect.size());

        Map<String, List<User>> collect1 = users.stream().collect(Collectors.groupingBy(s -> s.getName()));
        System.out.println(collect1.size());
        System.out.println("=");
        Map<String, Set<String>> collect2 = users.stream().collect(Collectors.groupingBy(s -> s.getName(), Collectors.mapping(s -> s.getPassword(), Collectors.toSet())));
        collect2.keySet().stream().forEach(System.out::println);
        collect2.values().stream().forEach(System.out::println);
        System.out.println("=");
        Map<String, Long> collect3 = users.stream().collect(Collectors.groupingBy(s -> s.getName(), Collectors.counting()));
        collect3.values().stream().forEach(System.out::println);


        System.out.println("==========");
        int reduce = IntStream.rangeClosed(1, 10).reduce(10, Integer::sum);
        System.out.println(reduce);



        Integer reduce1 = users.stream().map(i -> i.getAge()).reduce(0, Integer::sum);
        System.out.println(reduce1);

        int sum1 = users.stream().mapToInt(i -> i.getAge()).sum();
        System.out.println(sum1);
    }
}
