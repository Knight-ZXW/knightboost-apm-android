package com.knightboost.apm.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AndroidSystemProperty {
    private static Method getSystemPropertyMethod;

    private static Method getLongMethod;

    private static Method getIntMethod;

    private static Method setMethod;

    static {
        try {
            Method[] methods;
            int methodLength =(methods = Class.forName("android.os.SystemProperties").getMethods()).length;
            for (int i = 0; i < methodLength; i++) {
                Method method =methods[i];
                String methodName = method.getName();
                if (methodName.equals("get")) {
                    getSystemPropertyMethod = method;
                } else if (methodName.equals("set")) {
                    setMethod = method;
                } else if (methodName.equals("getLong")) {
                    AndroidSystemProperty.getLongMethod = method;
                } else if (methodName.equals("getInt")) {
                    getIntMethod = method;
                }
            }
        } catch (ClassNotFoundException classNotFoundException) {
        }
    }

    public static String getSystemProperty(String property, String defaultValue) {
        if (getSystemPropertyMethod == null)
            return defaultValue;
        try {
            return (String) getSystemPropertyMethod.invoke(null, new Object[] { property, defaultValue });
        } catch (IllegalArgumentException illegalArgumentException) {
        } catch (IllegalAccessException illegalAccessException) {
        } catch (InvocationTargetException invocationTargetException) {
        }
        return defaultValue;
    }
}
