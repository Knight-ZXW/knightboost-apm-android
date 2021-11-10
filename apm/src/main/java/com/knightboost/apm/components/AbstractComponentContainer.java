package com.knightboost.apm.components;

import com.knightboost.apm.inject.Provider;

import java.util.Set;

/**
 * created by Knight-ZXW on 2021/11/10
 */
abstract class AbstractComponentContainer implements ComponentContainer{
    @Override
    public <T> T get(Class<T> anInterface) {
        Provider<T> provider = getProvider(anInterface);
        if (provider == null) {
            return null;
        }
        return provider.get();
    }

    @Override
    public <T> Set<T> setOf(Class<T> anInterface) {
        return setOfProvider(anInterface).get();
    }
}
