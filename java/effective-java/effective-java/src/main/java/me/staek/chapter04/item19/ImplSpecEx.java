package me.staek.chapter04.item19;

/**
 * javadoc -d target/apidoc src/main/java/me/whiteship/chapter04/item19/impespec/ExtendableClass.java -tag "implSpec:a:Implementation Ruquirements:"
 */
public class ImplSpecEx {

    /**
     * This method can be overridden to print any message.
     *
     * @implSpec
     * Please use System.out.println().
     */
    protected void doSomething() {
        System.out.println("hello");
    }
}
