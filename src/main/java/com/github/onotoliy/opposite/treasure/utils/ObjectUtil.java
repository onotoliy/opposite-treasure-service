package com.github.onotoliy.opposite.treasure.utils;

public class ObjectUtil {

    public static final ObjectUtil OBJECT = new ObjectUtil();

    public <T> boolean nonEqually(T x, T y) {
        return !isEqually(x, y);
    }

    public <T> boolean isEqually(T x, T y) {
        return isEqually(x, y, Object::equals);
    }

    public <T> boolean nonEqually(T x, T y, Equal<T> equal) {
        return !isEqually(x, y, equal);
    }

    public <T> boolean isEqually(T x, T y, Equal<T> equal) {
        if (x == null && y == null) {
            return true;
        }

        if (x == null || y == null) {
            return false;
        }

        return equal.equals(x, y);
    }

    public boolean nonEmpty(Object value) {
        return !isEmpty(value);
    }

    public boolean isEmpty(Object value) {
        return value == null;
    }

    @FunctionalInterface
    public interface Equal<T> {
        boolean equals(T o1, T o2);
    }

}
