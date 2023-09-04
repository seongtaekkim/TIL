package me.staek.chapter04.item15;

import java.util.Arrays;

class DefaultMemberService implements MemberService {

    private String name;

    public static class PrivateStaticClass {
    }


    public static void main(String[] args) {

        // class 정보에 PrivateStaticClass class 는 존재하지 않는다.
        Arrays.stream(PrivateStaticClass.class.getDeclaredFields()).forEach(System.out::println);
    }

}
