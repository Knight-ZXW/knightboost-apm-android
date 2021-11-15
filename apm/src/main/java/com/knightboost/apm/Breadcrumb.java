package com.knightboost.apm;

import androidx.annotation.NonNull;

/**
 * created by Knight-ZXW on 2021/11/15
 */
public class Breadcrumb {

    @NonNull
    private String type;
    @NonNull
    private String message;

    public Breadcrumb(@NonNull String type,@NonNull String message){
        this.type = type;
        this.message = message;
    }
}
