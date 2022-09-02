package com.knightboost.apm.instrumentation;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.knightboost.apm.common.util.Clock;
import com.knightboost.lancet.api.Origin;
import com.knightboost.lancet.api.Scope;
import com.knightboost.lancet.api.This;
import com.knightboost.lancet.api.annotations.Group;
import com.knightboost.lancet.api.annotations.Insert;
import com.knightboost.lancet.api.annotations.TargetClass;
import com.knightboost.lancet.api.annotations.TargetMethod;
import com.knightboost.lancet.api.annotations.Weaver;

@Weaver
@Group("apm")
public class ActivityMethodWeaver {
    @Insert(mayCreateSuper = true)
    @TargetMethod(methodName = "onCreate")
    @TargetClass(value = "androidx.appcompat.app.AppCompatActivity",scope = Scope.ALL_CHILDREN)
    public void onCreate(@Nullable Bundle saveInstanceState){

        long begin = Clock.getCurrentTimestampMicros();
        Origin.callVoid();
        long end = Clock.getCurrentTimestampMicros();
        Activity activity = (Activity) This.get();

    }

    @Insert(mayCreateSuper = true)
    @TargetMethod(methodName = "onStart")
    @TargetClass(value = "androidx.appcompat.app.AppCompatActivity",scope = Scope.ALL_CHILDREN)
    public void  onStart(){

    }

    @Insert(mayCreateSuper = true)
    @TargetMethod(methodName = "onResume")
    @TargetClass(value = "androidx.appcompat.app.AppCompatActivity",scope = Scope.ALL_CHILDREN)
    public void  onResume(){

    }
}
