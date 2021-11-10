package com.knightboost.apm.components;


import com.knightboost.apm.inject.Provider;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Lazy mutable thread-safe {@link com.google.firebase.inject.Provider} for {@link Set}s.
 *
 * <p>The actual set is materialized only when first requested via {@link #get()}.
 *
 * <p>As new values are added to the set via {@link #add(com.knightboost.apm.inject.Provider)}, the underlying set is updated
 * with the new value.
 */
class LazySet<T> implements Provider<Set<T>> {

    private volatile Set<Provider<T>> providers;
    private volatile Set<T> actualSet = null;

    LazySet(Collection<Provider<T>> providers) {
        this.providers = Collections.newSetFromMap(new ConcurrentHashMap<>());
        this.providers.addAll(providers);
    }

    static LazySet<?> fromCollection(Collection<Provider<?>> providers) {
        @SuppressWarnings("unchecked")
        Collection<Provider<Object>> casted = (Collection<Provider<Object>>) (Set<?>) providers;
        return new LazySet<>(casted);
    }

    @Override
    public Set<T> get() {
        if (actualSet == null) {
            synchronized (this) {
                if (actualSet == null) {
                    actualSet = Collections.newSetFromMap(new ConcurrentHashMap<>());
                    updateSet();
                }
            }
        }
        return Collections.unmodifiableSet(actualSet);
    }

    synchronized void add(Provider<T> newProvider) {
        if (actualSet == null) {
            providers.add(newProvider);
        } else {
            actualSet.add(newProvider.get());
        }
    }

    private synchronized void updateSet() {
        for (Provider<T> provider : providers) {
            actualSet.add(provider.get());
        }
        providers = null;
    }
}

