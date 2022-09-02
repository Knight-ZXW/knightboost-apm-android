package com.knightboost.apm;

import android.util.Log;

public class ApmSafety {

    public static void handleException(String message,Throwable e){
        handleException("apmSdk",message,e);
    }

    public static void handleException(Throwable e){
        handleException("un expected exception happen ",e);
    }


    public static void handleException(String taskTag,String message,Throwable e){
        Log.e("APMSDK", message, e);
    }
}