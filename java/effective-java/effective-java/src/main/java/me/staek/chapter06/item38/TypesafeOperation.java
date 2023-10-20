package me.staek.chapter06.item38;
import java.util.Arrays;


/**
 * 타입안전열거패턴
 */
public class TypesafeOperation {
    private final String type;
    private TypesafeOperation(String type) {
        this.type = type;
    }

    public String toString() {
        return type;
    }

    public static final TypesafeOperation PLUS = new TypesafeOperation("+");
    public static final TypesafeOperation MINUS = new TypesafeOperation("-");
    public static final TypesafeOperation TIMES = new TypesafeOperation("*");
    public static final TypesafeOperation DIVIDE = new TypesafeOperation("/");

    public static TypesafeOperation[] getTypes() {
        return new TypesafeOperation[] {PLUS, MINUS, TIMES, DIVIDE};
    }

    public static void main(String[] args) {
        Arrays.stream(TypesafeOperation.getTypes()).toList().forEach(System.out::println);
    }
}
