# Enum



### Enum

- enum class 에는 우리가 사용하는 valueof, values 메서드가 보이지 않음.
  (valuesof 는 시그니처 다름)

~~~java
public abstract class Enum<E extends Enum<E>>
        implements Constable, Comparable<E>, Serializable {

private final String name;

public final String name() { return name; }

private final int ordinal;

public final int ordinal() { return ordinal; }

protected Enum(String name, int ordinal) {
    this.name = name;
    this.ordinal = ordinal;
}

public String toString() {return name; }

public final boolean equals(Object other) {
    return this==other;
}

public final int hashCode() {
    return super.hashCode();
}

protected final Object clone() throws CloneNotSupportedException {
    throw new CloneNotSupportedException();
}

public final int compareTo(E o) {
    Enum<?> other = (Enum<?>)o;
    Enum<E> self = this;
    if (self.getClass() != other.getClass() && // optimization
        self.getDeclaringClass() != other.getDeclaringClass())
        throw new ClassCastException();
    return self.ordinal - other.ordinal;
}

@SuppressWarnings("unchecked")
public final Class<E> getDeclaringClass() {
    Class<?> clazz = getClass();
    Class<?> zuper = clazz.getSuperclass();
    return (zuper == Enum.class) ? (Class<E>)clazz : (Class<E>)zuper;
}

@Override
public final Optional<EnumDesc<E>> describeConstable() {
    return getDeclaringClass()
            .describeConstable()
            .map(c -> EnumDesc.of(c, name));
}

public static <T extends Enum<T>> T valueOf(Class<T> enumClass,
                                            String name) {
    T result = enumClass.enumConstantDirectory().get(name);
    if (result != null)
        return result;
    if (name == null)
        throw new NullPointerException("Name is null");
    throw new IllegalArgumentException(
        "No enum constant " + enumClass.getCanonicalName() + "." + name);
}

@SuppressWarnings("deprecation")
protected final void finalize() { }

@java.io.Serial
private void readObject(ObjectInputStream in) throws IOException,
    ClassNotFoundException {
    throw new InvalidObjectException("can't deserialize enum");
}

@java.io.Serial
private void readObjectNoData() throws ObjectStreamException {
    throw new InvalidObjectException("can't deserialize enum");
}

public static final class EnumDesc<E extends Enum<E>>
        extends DynamicConstantDesc<E> {

    private EnumDesc(ClassDesc constantClass, String constantName) {
        super(ConstantDescs.BSM_ENUM_CONSTANT, requireNonNull(constantName), requireNonNull(constantClass));
    }

    public static<E extends Enum<E>> EnumDesc<E> of(ClassDesc enumClass,
                                                    String constantName) {
        return new EnumDesc<>(enumClass, constantName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E resolveConstantDesc(MethodHandles.Lookup lookup)
            throws ReflectiveOperationException {
        return Enum.valueOf((Class<E>) constantType().resolveConstantDesc(lookup), constantName());
    }

    @Override
    public String toString() {
        return String.format("EnumDesc[%s.%s]", constantType().displayName(), constantName());
    }
}
}

~~~





### bytecode

- 컴파일러가 values, valueof 함수를 생성해서 추가해줌.

-  [oracle docs](https://docs.oracle.com/javase/specs/jls/se7/html/jls-8.html#jls-8.9.2)

- > *It follows that enum type declarations cannot contain fields that conflict with the enum constants, and cannot contain methods that conflict with the automatically generated methods (*`values()` *and* `valueOf(String)`*) or methods that override the* `final` *methods in Enum (*`equals(Object)`*,* `hashCode()`*,* `clone()`*,* `compareTo(Object)`*,* `name()`*,* `ordinal()`*, and* `getDeclaringClass()`*).*

~~~java
public class test {
    enum aa{a,b,c;}
    public static void main(String[] args) {}
}
~~~

~~~java
inal enum me/staek/enumset/test$aa extends java/lang/Enum {

  // compiled from: test.java
  NESTHOST me/staek/enumset/test
  // access flags 0x4018
  final static enum INNERCLASS me/staek/enumset/test$aa me/staek/enumset/test aa

  // access flags 0x101A
  private final static synthetic [Lme/staek/enumset/test$aa; $VALUES

  // access flags 0x9
  public static values()[Lme/staek/enumset/test$aa;
   L0
    LINENUMBER 4 L0
    GETSTATIC me/staek/enumset/test$aa.$VALUES : [Lme/staek/enumset/test$aa;
    INVOKEVIRTUAL [Lme/staek/enumset/test$aa;.clone ()Ljava/lang/Object;
    CHECKCAST [Lme/staek/enumset/test$aa;
    ARETURN
    MAXSTACK = 1
    MAXLOCALS = 0

  // access flags 0x9
  public static valueOf(Ljava/lang/String;)Lme/staek/enumset/test$aa;
   L0
    LINENUMBER 4 L0
    LDC Lme/staek/enumset/test$aa;.class
    ALOAD 0
    INVOKESTATIC java/lang/Enum.valueOf (Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;
    CHECKCAST me/staek/enumset/test$aa
    ARETURN
   L1
    LOCALVARIABLE name Ljava/lang/String; L0 L1 0
    MAXSTACK = 2
    MAXLOCALS = 1

  // access flags 0x2
  // signature ()V
  // declaration: void <init>()
  private <init>(Ljava/lang/String;I)V
   L0
    LINENUMBER 4 L0
    ALOAD 0
    ALOAD 1
    ILOAD 2
    INVOKESPECIAL java/lang/Enum.<init> (Ljava/lang/String;I)V
    RETURN
   L1
    LOCALVARIABLE this Lme/staek/enumset/test$aa; L0 L1 0
    MAXSTACK = 3
    MAXLOCALS = 3

  // access flags 0x100A
  private static synthetic $values()[Lme/staek/enumset/test$aa;
   L0
    LINENUMBER 4 L0
    ICONST_0
    ANEWARRAY me/staek/enumset/test$aa
    ARETURN
    MAXSTACK = 1
    MAXLOCALS = 0
~~~

