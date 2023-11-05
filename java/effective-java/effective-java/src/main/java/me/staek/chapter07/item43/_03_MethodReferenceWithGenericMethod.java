package me.staek.chapter07.item43;

class MyInfo {
    static String myNameis() {
        return "staek";
    }
}

interface G1 {  <E extends Exception> Object m() throws E; }
interface G2 {  <F extends Exception> String m() throws Exception; }
interface G extends G1, G2 {}


public class _03_MethodReferenceWithGenericMethod {
    static String check(G g) throws Exception {
        return g.m();
    }

    public static void main(String args[]) throws Exception {
//        그니까 ... myNameis 말고도 Object 반환 타입하는 customMethod라는게 있으면...
//
//        당연히 되겠지 해보고 와야게따
//        String name = check(() -> MyInfo.myNameis()); //
        String name = check(MyInfo::myNameis);
        System.out.println(name);
    }
}
