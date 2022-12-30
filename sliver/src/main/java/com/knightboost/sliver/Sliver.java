package com.knightboost.sliver;

import android.annotation.SuppressLint;

import java.lang.reflect.Field;

public class Sliver {

    //Flag indicating if the sliver already has been initialized
    private static boolean initialized = false;
    private static boolean initializeSuccess = false;

    public static void init() {
        if (!initialized) {
            initialized = true;
            try {
                System.loadLibrary("sliver.so");
            } catch (Throwable e) {
                initializeSuccess =false;
                //TODO add native init function
            }
        }
    }

    public static boolean isInit(){
        return initialized;
    }

    public static String getSackTrace(Thread thread){
        long threadPeer = getThreadPeer(thread);
        nGetStackTrace(thread,threadPeer);
        return "";
    }

    public static native void nGetStackTrace(Thread thread,long threadPeer);


    /**
     *
     * @param thread thread
     * @return threadPeer value of the thread
     */
    @SuppressWarnings("ConstantConditions")
    @SuppressLint("DiscouragedPrivateApi")
    public static long getThreadPeer(Thread thread){
        try {
            Field nativePeerField = Thread.class.getDeclaredField("nativePeer");
            nativePeerField.setAccessible(true);
            return (long) nativePeerField.get(thread);
        } catch (NoSuchFieldException e) {
            //should never happen
            e.printStackTrace();
            return -1;
        } catch (IllegalAccessException e) {
            //should never happen
            e.printStackTrace();
            return -1;
        }

    }
}
