package com.knightboost.moonlight.util;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * created by Knight-ZXW on 2021/11/9
 */

/**
 * Util class for Collections
 */
public final class CollectionUtils {

    private CollectionUtils() {
    }

    /**
     * Returns an Iterator size
     *
     * @param data the Iterable
     * @return iterator size
     */
    public static int size(final @NotNull Iterable<?> data) {
        if (data instanceof Collection) {
            return ((Collection<?>) data).size();
        }
        int counter = 0;
        for (Object ignored : data) {
            counter++;
        }
        return counter;
    }

    /**
     * Creates a new {@link ConcurrentHashMap} as a shallow copy of map given by parameter.
     *
     * @param map the map to copy
     * @param <K> the type of map keys
     * @param <V> the type of map values
     * @return the shallow copy of map
     */
    public static <K, V>  Map<K,  V> newConcurrentHashMap(
             Map<K, V> map) {
        if (map != null) {
            return new ConcurrentHashMap<>(map);
        } else {
            return null;
        }
    }

    /**
     * Creates a new {@link HashMap} as a shallow copy of map given by parameter.
     *
     * @param map the map to copy
     * @param <K> the type of map keys
     * @param <V> the type of map values
     * @return a new {@link HashMap} or {@code null} if parameter is {@code null}
     */
    public static <K, V>  Map<K,  V> newHashMap( Map<K,  V> map) {
        if (map != null) {
            return new HashMap<>(map);
        } else {
            return null;
        }
    }

    /**
     * Creates a new {@link ArrayList} as a shallow copy of list given by parameter.
     *
     * @param list the list to copy
     * @param <T>  the type of list entries
     * @return a new {@link ArrayList} or {@code null} if parameter is {@code null}
     */
    public static <T>  List<T> newArrayList( List<T> list) {
        if (list != null) {
            return new ArrayList<>(list);
        } else {
            return null;
        }
    }

    /**
     * Returns a new map which entries match a predicate specified by a parameter.
     *
     * @param map       - the map to filter
     * @param predicate - the predicate
     * @param <K>       - map entry key type
     * @param <V>       - map entry value type
     * @return a new map
     */
    public static @NotNull
    <K, V> Map<K, V> filterMapEntries(
            final @NotNull Map<K, V> map, final @NotNull Predicate<Map.Entry<K, V>> predicate) {
        final Map<K, V> filteredMap = new HashMap<>();
        for (final Map.Entry<K, V> entry : map.entrySet()) {
            if (predicate.test(entry)) {
                filteredMap.put(entry.getKey(), entry.getValue());
            }
        }
        return filteredMap;
    }

    /**
     * A simplified copy of Java 8 Predicate.
     *
     * @param <T> the type
     */
    public interface Predicate<T> {
        boolean test(T t);
    }
}
