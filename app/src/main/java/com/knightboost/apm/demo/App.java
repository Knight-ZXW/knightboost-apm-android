package com.knightboost.apm.demo;

import android.app.Application;
import android.content.Context;

import com.wind.hiddenapi.bypass.HiddenApiBypass;

import me.weishu.reflection.Reflection;

public class App extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Reflection.unseal(base);
        // HiddenApiBypass.startBypass();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

}
