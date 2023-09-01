package me.staek.issue.checked_exception_performance;

public class CheckedExceptionTimeTest {
    static int LIMIT = 1_000_000;
    public static void main(String[] args) {
//      Depth d = new Depth();
        CustomDepth d = new CustomDepth();
        long beforeTime = System.currentTimeMillis();

        for (int i = 0 ; i < LIMIT ; i++ ) {
            try {
                d.method1();
            } catch (CustemException e) {
                e.getStackTrace();
            }
        }

        long afterTime = System.currentTimeMillis();
        long secDiffTime = (afterTime - beforeTime);
        System.out.println("time: "+secDiffTime);
    }
}
