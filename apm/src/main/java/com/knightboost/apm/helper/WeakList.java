package com.knightboost.apm.helper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * created by Knight-ZXW on 2021/11/15
 */
class WeakList<T> extends ArrayList<WeakReference<T>> {
    @Override
    public boolean add(WeakReference<T> tWeakReference) {
        return super.add(tWeakReference);
    }
}
