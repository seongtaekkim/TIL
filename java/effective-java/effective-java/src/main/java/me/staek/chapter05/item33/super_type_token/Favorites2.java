package me.staek.chapter05.item33.super_type_token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TypeRef 사용예제
 * https://gafter.blogspot.com/2007/05/limitation-of-super-type-tokens.html
 */
public class Favorites2 {

    private final Map<TypeRef<?>, Object> favorites = new HashMap<>();

    public <T> void put(TypeRef<T> typeRef, T thing) {
        favorites.put(typeRef, thing);
    }

    /**
     * typeRref는 T를 인스턴스로서 관리하지 않으므로 Class.cast() 사용이 불가하다.
     */
//    @SuppressWarnings("unchecked")
    public <T> T get(TypeRef<T> typeRref) {
//        return typeRref.getType().getClass().cast(favorites.get(typeRref)); // Type의 인스턴스를 검사하므로 매칭이 안됨
//        return ((Class<T>)typeRref.getType()).cast(favorites.get(typeRref)); // Class<T> 로 캐스트 해도 경고가 발생함.
        return (T) (favorites.get(typeRref));
    }

    public static void main(String[] args) {
        Favorites2 f = new Favorites2();

        TypeRef<List<String>> stringTypeRef = new TypeRef<>() {};
        System.out.println(stringTypeRef.getType());

        TypeRef<List<Integer>> integerTypeRef = new TypeRef<>() {};
        System.out.println(integerTypeRef.getType());

        TypeRef<String> stringex = new TypeRef<>() {};
        System.out.println(stringex.getType());

        f.put(stringTypeRef, List.of("a", "b", "c"));
        f.put(integerTypeRef, List.of(1, 2, 3));
        f.get(stringTypeRef).forEach(System.out::println);
        f.get(integerTypeRef).forEach(System.out::println);

        f.put(stringex, "apple");
        System.out.println(f.get(stringex));

    }
}
