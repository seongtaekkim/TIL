package me.staek.chapter04.item24;

/**
 * local class
 * - 거의 사용하지 않는다.
 * - 차라리 외부로 빼는 게 낫다. - 이미 가독성이 안좋아 보임.
 */
public class LocalClassEx {

    private int number = 10;

    void doSomething() {
        class LocalClass {
            private void printNumber() {
                System.out.println(number);
            }
        }

        LocalClass localClass = new LocalClass();
        localClass.printNumber();
    }

    public static void main(String[] args) {
        LocalClassEx myClass = new LocalClassEx();
        myClass.doSomething();
    }
}
