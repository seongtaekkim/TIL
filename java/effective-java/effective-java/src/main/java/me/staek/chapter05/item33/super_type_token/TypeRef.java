package me.staek.chapter05.item33.super_type_token;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * neal gafter's super type token 예제
 * https://gafter.blogspot.com/2006/12/super-type-tokens.html
 */
public abstract class TypeRef<T> {
    /**
     * Type : String.class, Intger.class ...
     * List<Type>
     * Type
     */
    private final Type type;

    protected TypeRef() {
        ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
        type = superclass.getActualTypeArguments()[0];
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof TypeRef && ((TypeRef)o).type.equals(type);
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    public Type getType() {
        return type;
    }
}
