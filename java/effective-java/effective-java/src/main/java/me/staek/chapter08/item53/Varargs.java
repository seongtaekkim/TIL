package me.staek.chapter08.item53;

public class Varargs {

    /**
     * 일반적인 가변인자 사용코드
     */
    static int sum(int... args) {
        int sum = 0;
        for (int arg : args)
            sum += arg;
        return sum;
    }

    /**
     * 최소 1개이상 인자가 필수일때, 예외처리를 통해 확인한다. (런타임에 확인)
     */
//    static int min(int... args) {
//        if (args.length == 0)
//            throw new IllegalArgumentException("Too few arguments");
//        int min = args[0];
//        for (int i = 1; i < args.length; i++)
//            if (args[i] < min)
//                min = args[i];
//        return min;
//    }

    /**
     * 최소 1개 이상 인자필수일때, 파라메터를 하나 늘린다. (컴파일타임 확인)
     * -> 얖에 코드보다 더 안전하다.
     */
    static int min(int firstArg, int... remainingArgs) {
        int min = firstArg;
        for (int arg : remainingArgs)
            if (arg < min)
                min = arg;
        return min;
    }

    public static void main(String[] args) {
        System.out.println(sum());
        System.out.println(sum(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        System.out.println(min(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    }
}
