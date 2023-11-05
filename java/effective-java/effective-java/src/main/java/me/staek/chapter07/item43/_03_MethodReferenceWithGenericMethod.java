package me.staek.chapter07.item43;

import java.util.function.Supplier;

class MyInfo {
    static String myNameis() {
        return "staek";
    }
}

interface G1 {  <E extends Exception> Object m() throws E; }
interface G2 {  <F extends Exception> String m() throws Exception; }
interface G extends G1, G2 {}


/**
 * https://docs.oracle.com/javase/specs/jls/se8/html/jls-9.html#jls-9.9
 * Example 9.9-2. Generic Function Types
 *
 * 메서드레퍼런스에는 동작하고 람다는 동작하는 예제 작성.
 * - 인터페이스에 선언된 제네릭 메서드에 경우, 람다에서는 제네릭 메서드타입을 선언하는 문법이 없어 동작하지 않는다.
 */
public class _03_MethodReferenceWithGenericMethod {
    static String check(G g) throws Exception {
        return g.m();
    }

    public static void main(String args[]) throws Exception {
//        그니까 ... myNameis 말고도 Object 반환 타입하는 customMethod라는게 있으면...
//
//        당연히 되겠지 해보고 와야게따
//        String name = check(() -> MyInfo.myNameis()); //
        Supplier<String> myNameis = MyInfo::myNameis;
        String name = check(MyInfo::myNameis);
        System.out.println(name);
    }
}
