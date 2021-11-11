package com.knightboost.moonlight.inject;


import androidx.annotation.NonNull;


/**
 * Represents a continuation-style dependency.
 * The motivation for it is to model optional dependencies that may become available in the future and once they do,
 * the depender will get notified automatically via the registered Deferred.DeferredHandler.
 * Example:
 *
 *  class Foo {
 *    Foo(Deferred<Bar> bar) {
 *      bar.whenAvailable(barProvider -> {
 *        // automatically called when Bar becomes available
 *        use(barProvider.get());
 *      });
 *    }
 *  }
 *
 * created by Knight-ZXW on 2021/11/10
 */
public interface Deferred<T> {
    interface DeferredHandler<T> {
        void handle(Provider<T> provider);
    }

    /** Register a callback that is executed once {@link T} becomes available */
    void whenAvailable(@NonNull DeferredHandler<T> handler);
}
