package com.knightboost.moonlight.components;

/**
 * created by Knight-ZXW on 2021/11/10
 */


import com.knightboost.moonlight.inject.Deferred;
import com.knightboost.moonlight.inject.Provider;

import java.util.Set;

/** Provides a means to retrieve instances of requested classes/interfaces. */
public interface ComponentContainer {
    <T> T get(Class<T> anInterface);

    <T> Provider<T> getProvider(Class<T> anInterface);

    <T> Deferred<T> getDeferred(Class<T> anInterface);

    <T> Set<T> setOf(Class<T> anInterface);

    <T> Provider<Set<T>> setOfProvider(Class<T> anInterface);
}

