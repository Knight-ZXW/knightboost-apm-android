package com.knightboost.apm.components;

/**
 * created by Knight-ZXW on 2021/11/10
 */
public final class Preconditions {

    public static void checkArgument(boolean expression, String errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    public static <T> T checkNotNull(T reference, String errorMessage) {
        if (reference == null) {
            throw new NullPointerException(errorMessage);
        }
        return reference;
    }

    public static void checkState(boolean expression, String errorMesage) {
        if (!expression) {
            throw new IllegalStateException(errorMesage);
        }
    }
}
