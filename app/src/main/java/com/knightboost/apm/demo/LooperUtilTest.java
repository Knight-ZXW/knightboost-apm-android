package com.knightboost.apm.demo;

import android.os.Message;
import android.util.Log;

import com.knightboost.freeandroid.LooperMessageObserver;
import com.knightboost.freeandroid.LooperUtil;

public class LooperUtilTest {

    public static void looperMonitor(){
        LooperUtil.setObserver(new LooperMessageObserver() {
            @Override
            public Object messageDispatchStarting() {
                Log.e("looper","messageDispatchStarting");
                return null;
            }

            @Override
            public void messageDispatched(Object token, Message msg) {
                Log.e("looper","messageDispatched "+msg);

            }

            @Override
            public void dispatchingThrewException(Object token, Message msg, Exception exception) {
                Log.e("looper", msg+"dispatchingThrewException");

            }
        });
    }
}
