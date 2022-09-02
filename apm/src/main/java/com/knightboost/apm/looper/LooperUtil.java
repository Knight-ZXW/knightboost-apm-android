package com.knightboost.apm.looper;

import android.annotation.SuppressLint;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.knightboost.freeandroid.LooperObserver;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class LooperUtil {
    public static void addObserver(){
        try {
            @SuppressLint("BlockedPrivateApi") Field sObserver = Looper.class.getDeclaredField("sObserver");
            sObserver.setAccessible(true);


            // Object curObserver = sObserver.get(null);
        //     MyLooperObserver myLooperObserver = new MyLooperObserver(){
        //         @Override
        //         public Object messageDispatchStarting() {
        //             Log.e("MyLooper","messageDispatchStarting");
        //             return super.messageDispatchStarting();
        //         }
        //
        //         @Override
        //         public void messageDispatched(Object token, Message msg) {
        //             Log.e("MyLooper","messageDispatched");
        //             super.messageDispatched(token, msg);
        //         }
        //
        //         @Override
        //         public void dispatchingThrewException(Object token, Message msg, Exception exception) {
        //             Log.e("MyLooper","dispatchingThrewException");
        //             super.dispatchingThrewException(token, msg, exception);
        //         }
        //     };
        //
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
