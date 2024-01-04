/*
 * Copyright © 2024 fluffydaddy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.fluffydaddy.jutils.reflect;

import io.fluffydaddy.jutils.collection.Array;
import io.fluffydaddy.jutils.collection.Pair;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for creating mutable collections and lists.
 */
public final class CollectionUtil {
    
    // Private constructor to prevent instantiation
    private CollectionUtil() {
    }
    
    /**
     * Creates a mutable set from a collection of elements.
     *
     * @param elements The collection of elements.
     * @param <T>      The type of elements in the set.
     * @return A mutable set containing the elements.
     */
    public static <T> Set<T> mutableSetOf(Collection<T> elements) {
        if (elements == null || elements.isEmpty()) {
            return new LinkedHashSet<>();
        }
        LinkedHashSet<T> mutableSet = new LinkedHashSet<>(elements.size());
        mutableSet.addAll(elements);
        return mutableSet;
    }
    
    /**
     * Creates a mutable set from an array of elements.
     *
     * @param elements The array of elements.
     * @param <T>      The type of elements in the set.
     * @return A mutable set containing the elements.
     */
    @SafeVarargs
    public static <T> Set<T> mutableSetOf(T... elements) {
        if (elements == null || elements.length == 0) {
            return new LinkedHashSet<>();
        }
        LinkedHashSet<T> mutableSet = new LinkedHashSet<>(elements.length);
        mutableSet.addAll(Arrays.asList(elements));
        return mutableSet;
    }
    
    /**
     * Creates a mutable list from a collection of elements.
     *
     * @param elements The collection of elements.
     * @param <T>      The type of elements in the list.
     * @return A mutable list containing the elements.
     */
    public static <T> Array<T> mutableListOf(Collection<T> elements) {
        if (elements == null || elements.isEmpty()) {
            return new Array<>();
        }
        Array<T> mutableList = new Array<>();
        mutableList.addAll(elements);
        return mutableList;
    }
    
    /**
     * Creates a mutable list from an array of elements.
     *
     * @param elements The array of elements.
     * @param <T>      The type of elements in the list.
     * @return A mutable list containing the elements.
     */
    @SafeVarargs
    public static <T> Array<T> mutableListOf(T... elements) {
        if (elements == null || elements.length == 0) {
            return new Array<>();
        }
        Array<T> mutableList = new Array<>();
        mutableList.addAll(Arrays.asList(elements));
        return mutableList;
    }
    
    /**
     * Creates a mutable map from an array of key-value pairs.
     *
     * @param elements The array of key-value pairs.
     * @param <K>      The type of keys in the map.
     * @param <V>      The type of values in the map.
     * @return A mutable map containing the key-value pairs.
     */
    @SafeVarargs
    public static <K, V> Map<K, V> mutableMapOf(Pair<K, V>... elements) {
        if (elements == null || elements.length == 0) {
            return new LinkedHashMap<>();
        }
        LinkedHashMap<K, V> mutableMap = new LinkedHashMap<>(elements.length);
        for (Pair<K, V> element : elements) {
            mutableMap.put(element.getFirst(), element.getSecond());
        }
        return mutableMap;
    }
    
    /**
     * Creates a mutable map from a collection of key-value pairs.
     *
     * @param elements The collection of key-value pairs.
     * @param <K>      The type of keys in the map.
     * @param <V>      The type of values in the map.
     * @return A mutable map containing the key-value pairs.
     */
    public static <K, V> Map<K, V> mutableMapOf(Collection<Pair<K, V>> elements) {
        if (elements == null || elements.isEmpty()) {
            return new LinkedHashMap<>();
        }
        LinkedHashMap<K, V> mutableMap = new LinkedHashMap<>(elements.size());
        for (Pair<K, V> element : elements) {
            mutableMap.put(element.getFirst(), element.getSecond());
        }
        return mutableMap;
    }
    
    /**
     * Creates a mutable map from an existing map.
     *
     * @param elements The existing map.
     * @param <K>      The type of keys in the map.
     * @param <V>      The type of values in the map.
     * @return A mutable map containing the key-value pairs from the existing map.
     */
    public static <K, V> Map<K, V> mutableMapOf(Map<K, V> elements) {
        if (elements == null || elements.isEmpty()) {
            return new LinkedHashMap<>();
        }
        LinkedHashMap<K, V> mutableMap = new LinkedHashMap<>(elements.size());
        mutableMap.putAll(elements);
        return mutableMap;
    }
    
    /**
     * Creates an immutable list from an array of elements.
     *
     * @param elements The array of elements.
     * @param <T>      The type of elements in the list.
     * @return An immutable list containing the elements.
     */
    @SafeVarargs
    public static <T> Array<T> listOf(T... elements) {
        if (elements == null || elements.length == 0) {
            return new Array<>();
        }
        Array<T> list = new Array<>(elements.length);
        list.addAll(Arrays.asList(elements));
        return list;
    }
    
    /**
     * Creates an immutable set from an array of elements.
     *
     * @param elements The array of elements.
     * @param <T>      The type of elements in the set.
     * @return An immutable set containing the elements.
     */
    @SafeVarargs
    public static <T> Set<T> setOf(T... elements) {
        if (elements == null || elements.length == 0) {
            return new HashSet<>();
        }
        HashSet<T> set = new HashSet<>(elements.length);
        set.addAll(Arrays.asList(elements));
        return set;
    }
}
