package com.github.onotoliy.opposite.treasure.utils;

import java.util.Comparator;

public final class Objects {

    private Objects() {

    }

    public static <T> boolean nonEqually(final T x, final T y) {
        return !isEqually(x, y);
    }

    public static <T> boolean isEqually(final T x, final T y) {
        return isEqually(x, y, (o1, o2) -> o1.equals(o2) ? 0 : -1);
    }

    public static <T> boolean nonEqually(final T x,
                                         final T y,
                                         final Comparator<T> equal) {
        return !isEqually(x, y, equal);
    }

    public static <T> boolean isEqually(final T x,
                                        final T y,
                                        final Comparator<T> equal) {
        if (x == null && y == null) {
            return true;
        }

        if (x == null || y == null) {
            return false;
        }

        return equal.compare(x, y) == 0;
    }

    public static boolean nonEmpty(final Object value) {
        return !isEmpty(value);
    }

    public static boolean isEmpty(final Object value) {
        return value == null;
    }
}
