package com.knightboost.sliver;

import android.annotation.SuppressLint;
import android.util.Log;

import java.lang.reflect.Field;

public class Sliver {

    //Flag indicating if the sliver already has been initialized
    private static boolean initialized = false;
    private static boolean initializeSuccess = false;

    public static void init() {
        if (!initialized) {
            initialized = true;
            try {
                System.loadLibrary("xdl");
                System.loadLibrary("bytehook");
                System.loadLibrary("sliver");
            } catch (Throwable e) {
                e.printStackTrace();
                initializeSuccess =false;
                //TODO add native init function
            }
        }
    }

    public static boolean isInit(){
        return initialized;
    }

    /**
     * 注：不能在当前线程获取同线程的调用栈
     * @param thread
     * @return
     */
    public static String getSackTrace(Thread thread){
        long threadPeer = getThreadPeer(thread);
        Log.e("zxw","threadPeer is"+threadPeer);
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
