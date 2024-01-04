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

package io.fluffydaddy.jutils.collection;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * The {@code Table<T, K, V>} class represents a table of values indexed by two keys, where each cell in the table
 * is identified by a pair of keys (type and name). It provides various methods to manipulate and access values in the table.
 * </p>
 *
 * @param <T> The type of the first key
 * @param <K> The type of the second key
 * @param <V> The type of the value
 */
public class Table<T, K, V> {
    private final Map<T, Map<K, V>> table;
    
    /**
     * Private constructor to create a Table instance with a custom map.
     *
     * @param table The custom map to use for the table
     */
    private Table(Map<T, Map<K, V>> table) {
        this.table = table;
    }
    
    /**
     * Creates a Table instance with a custom map.
     *
     * @param table The custom map to use for the table
     * @param <T>   The type of the first key
     * @param <K>   The type of the second key
     * @param <V>   The type of the value
     * @return A Table instance with the provided map
     */
    public static <T, K, V> Table<T, K, V> custom(Map<T, Map<K, V>> table) {
        return new Table<>(table);
    }
    
    /**
     * Creates a Table instance with a concurrent map.
     *
     * @param <T> The type of the first key
     * @param <K> The type of the second key
     * @param <V> The type of the value
     * @return A Table instance with a concurrent map
     */
    public static <T, K, V> Table<T, K, V> concurrent() {
        return new Table<>(new ConcurrentHashMap<>());
    }
    
    /**
     * Creates a Table instance with a HashMap.
     *
     * @param <T> The type of the first key
     * @param <K> The type of the second key
     * @param <V> The type of the value
     * @return A Table instance with a HashMap
     */
    public static <T, K, V> Table<T, K, V> hashMap() {
        return new Table<>(new HashMap<>());
    }
    
    /**
     * Computes the value for the specified key using the provided mapping function if the key is not already associated
     * with a value (or is mapped to null) and then associates it with the computed value. If the mapping function returns
     * null, the key is mapped to null. If the key is present in the map, its current value is returned without invoking
     * the mapping function.
     *
     * @param map             The map to compute the value in
     * @param key             The key whose value should be computed if absent
     * @param mappingFunction The function to compute the value
     * @param <K>             The type of the key
     * @param <V>             The type of the value
     * @return The current (existing or computed) value associated with the key
     */
    private static <K, V> V computeIfAbsent(Map<K, V> map, K key, Lazy<K, V> mappingFunction) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        return map.put(key, mappingFunction.invoke(key));
    }
    
    /**
     * Gets the value associated with the specified key or returns the default value if the key is not present in the map.
     *
     * @param map The map to get the value from
     * @param key The key whose associated value is to be returned
     * @param def The default value to return if the key is not present in the map
     * @param <K> The type of the key
     * @param <V> The type of the value
     * @return The value associated with the key, or the default value if the key is not present in the map
     */
    private static <K, V> V getOrDefault(Map<K, V> map, K key, V def) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        return def;
    }
    
    /**
     * Computes the value for the specified type and name, using the provided value.
     *
     * @param type  The first key
     * @param name  The second key
     * @param value The value to compute if absent
     * @return The computed value
     */
    private V computeIfAbsent(T type, K name, V value) {
        return computeIfAbsent(table, type, key -> new HashMap<>()).put(name, value);
    }
    
    /**
     * Adds a value to the table with the specified type and name.
     *
     * @param type  The first key
     * @param name  The second key
     * @param value The value to add
     * @return The previous value associated with the specified type and name, or null if there was no mapping for the type and name
     */
    public V put(T type, K name, V value) {
        return computeIfAbsent(type, name, value);
    }
    
    /**
     * Retrieves the value associated with the specified type and name.
     *
     * @param type The first key
     * @param name The second key
     * @return The value associated with the specified type and name, or null if there is no mapping for the type and name
     */
    public V get(T type, K name) {
        return getOrDefault(table, type, Collections.emptyMap()).get(name);
    }
    
    /**
     * Checks if the table contains a mapping for the specified type and name.
     *
     * @param type The first key
     * @param name The second key
     * @return true if the table contains a mapping for the specified type and name, false otherwise
     */
    public boolean contains(T type, K name) {
        return table.containsKey(type) && table.get(type).containsKey(name);
    }
    
    /**
     * Removes the mapping for the specified type and name.
     *
     * @param type The first key
     * @param name The second key
     * @return The previous value associated with the specified type and name, or null if there was no mapping
     */
    public V remove(T type, K name) {
        return getOrDefault(table, type, Collections.emptyMap()).remove(name);
    }
    
    /**
     * Returns an array of entries representing the table.
     *
     * @return An array of entries representing the table
     */
    public Entry<T, K, V>[] toArray() {
        Array<Entry<T, K, V>> entries = new Array<>();
        for (Map.Entry<T, Map<K, V>> outerEntry : table.entrySet()) {
            T type = outerEntry.getKey();
            Map<K, V> innerMap = outerEntry.getValue();
            for (Map.Entry<K, V> innerEntry : innerMap.entrySet()) {
                K key = innerEntry.getKey();
                V value = innerEntry.getValue();
                entries.add(new Entry<>(type, key, value));
            }
        }
        return entries.toArray(new Entry[0]);
    }
    
    /**
     * Returns a set of entries representing the table.
     *
     * @return A set of entries representing the table
     */
    public Set<Entry<T, K, V>> entrySet() {
        Set<Entry<T, K, V>> entries = new HashSet<>();
        for (Map.Entry<T, Map<K, V>> outerEntry : table.entrySet()) {
            T type = outerEntry.getKey();
            Map<K, V> innerMap = outerEntry.getValue();
            for (Map.Entry<K, V> innerEntry : innerMap.entrySet()) {
                K key = innerEntry.getKey();
                V value = innerEntry.getValue();
                entries.add(new Entry<>(type, key, value));
            }
        }
        return entries;
    }
    
    /**
     * Returns an array of entries representing the table.
     *
     * @return An array of entries representing the table
     */
    public Array<Entry<T, K, V>> entries() {
        Array<Entry<T, K, V>> entries = new Array<>();
        for (Map.Entry<T, Map<K, V>> outerEntry : table.entrySet()) {
            T type = outerEntry.getKey();
            Map<K, V> innerMap = outerEntry.getValue();
            for (Map.Entry<K, V> innerEntry : innerMap.entrySet()) {
                K key = innerEntry.getKey();
                V value = innerEntry.getValue();
                entries.add(new Entry<>(type, key, value));
            }
        }
        return entries;
    }
    
    /**
     * Returns a collection of values representing the table.
     *
     * @return A collection of values representing the table
     */
    public Collection<V> values() {
        Array<V> values = new Array<>();
        
        for (Map<K, V> map : table.values()) {
            values.addAll(map.values());
        }
        
        return values;
    }
    
    /**
     * Clears the table, removing all mappings.
     */
    public void clear() {
        table.clear();
    }
    
    /**
     * The {@code Entry<T, K, V>} class represents an entry in the table, identified by a pair of keys (type and name).
     *
     * @param <T> The type of the first key
     * @param <K> The type of the second key
     * @param <V> The type of the value
     */
    public static class Entry<T, K, V> {
        private T type;
        private K key;
        private V value;
        
        /**
         * Constructs an entry with the specified type, key, and value.
         *
         * @param type  The first key
         * @param key   The second key
         * @param value The value
         */
        public Entry(T type, K key, V value) {
            this.type = type;
            this.key = key;
            this.value = value;
        }
        
        /**
         * Sets the type for the entry.
         *
         * @param type The first key
         * @return The entry with the updated type
         */
        public Entry<T, K, V> setType(T type) {
            this.type = type;
            return this;
        }
        
        /**
         * Gets the type of the entry.
         *
         * @return The first key
         */
        public T getType() {
            return type;
        }
        
        /**
         * Sets the key for the entry.
         *
         * @param key The second key
         * @return The entry with the updated key
         */
        public Entry<T, K, V> setKey(K key) {
            this.key = key;
            return this;
        }
        
        /**
         * Gets the key of the entry.
         *
         * @return The second key
         */
        public K getKey() {
            return key;
        }
        
        /**
         * Sets the value for the entry.
         *
         * @param value The value
         * @return The entry with the updated value
         */
        public Entry<T, K, V> setValue(V value) {
            this.value = value;
            return this;
        }
        
        /**
         * Gets the value of the entry.
         *
         * @return The value
         */
        public V getValue() {
            return value;
        }
        
        /**
         * Checks if the entry equals the specified type and key.
         *
         * @param type The first key to check
         * @param key  The second key to check
         * @return true if the entry equals the specified type and key, false otherwise
         */
        public boolean equals(T type, K key) {
            return getType() == type && getKey() == key;
        }
        
        /**
         * Checks if the entry equals the specified entry.
         *
         * @param entry The entry to check
         * @return true if the entry equals the specified entry, false otherwise
         */
        public boolean equals(Entry<T, K, V> entry) {
            return equals(entry.getType(), entry.getKey());
        }
        
        /**
         * Generates a hash code for the entry.
         *
         * @return The hash code
         */
        @Override
        public int hashCode() {
            return Objects.hash(type, key);
        }
        
        /**
         * Generates a string representation of the entry.
         *
         * @return A string representation of the entry
         */
        @Override
        public String toString() {
            return "Entry{" +
                    "type=" + type +
                    ", key=" + key +
                    ", value=" + value +
                    '}';
        }
    }
}
