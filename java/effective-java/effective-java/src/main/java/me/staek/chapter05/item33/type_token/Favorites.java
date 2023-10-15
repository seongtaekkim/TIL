package me.staek.chapter05.item33.type_token;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 타입 안전 이종 컨테이너 구현을 위한 type token 활용 예제
 */
public class Favorites {

    /**
     * 와일드카드 : Map이 아닌 Map의 key가 와일드카드 타입이다.
     */
    private Map<Class<?>, Object> map = new HashMap<>();

    /**
     * - key,value에 T 가 붙어 있으므로 다른 타입을 입력할 수 없도록 하였다.
     * - Class.cast() : isInstance() 를 이용하여 런타임에 해당 클래스의 인스턴스인지 판단.
     *   => put 단계에서 빠르게 예외여부를 판단할 수 있다. (컴파일 단계에서는 알 수 없음)
     */
    public <T> void put(Class<T> clazz, T value) {
        this.map.put(Objects.requireNonNull(clazz), clazz.cast(value));
    }

    /**
     * Class.cast() : 검사를 하고 형변환 한다는 의미.
     */
    public <T> T get(Class<T> clazz) {
        return clazz.cast(this.map.get(clazz));
    }

    /**
     * @SuppressWarnings : 검사를 하지 않고 형변환한다는 의미
     */
//    @SuppressWarnings("unchecked")
//    public <T> T get(Class<T> clazz) {
//        return (T)this.map.get(clazz);
//    }


    public static void main(String[] args) {
        Favorites favorites = new Favorites();
        Class<String> a = String.class;

        favorites.put(String.class, "staek");
        String i = favorites.get(String.class);
        System.out.println(i);

        /**
         * typetoken 문제점 1)
         * 악의적으로 rawtype으로 변경한 경우 컴파일에러에서는 잡을 수 없다.
         */
        favorites.put((Class)Integer.class, "3");
        favorites.get(Integer.class);

//        favorites.put(List.class, List.of(1,2,3));
//        List list = favorites.get(List.class);
//        list.stream().forEach(System.out::println);

        /**
         * typetoken 문제점 2)
         * 타입을 가진 List<Integer>.class 리터럴은 없음
         * 제네릭은 컴파일이후 타입이 소걷되기 때문에
         * List<Integer>.class 와 List<Integer>.class 를 비고할 수 없다.
         * 그래서 아래의 코드는 불가능 => 수퍼타입토큰으로 해결
         */
//        favorites.put(List<Integer>.class, List.of(1, 2, 3));
//        favorites.put(List<String>.class, List.of("a", "b", "c"));

    }

}
