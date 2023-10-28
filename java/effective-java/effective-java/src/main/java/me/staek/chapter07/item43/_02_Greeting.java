
package me.staek.chapter07.item43;

public class _02_Greeting {
    private String name;

    public _02_Greeting() {

    }
    public _02_Greeting(String name) {
        this.name = name;
    }
    public String hello(String name) {
        return ("hello " + name);
    }

    public static String hi(String name) {
        return ("hi " + name);
    }

    public String getName()
    {
        return (this.name);
    }
}
