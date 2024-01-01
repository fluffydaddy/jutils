package io.fluffydaddy.jutils;

import java.util.Collections;
import java.util.Collection;
import java.util.Objects;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Table<T, K, V> {
    private final Map<T, Map<K, V>> table;

    private Table(Map<T, Map<K, V>> table) {
        this.table = table;
    }

    public static <T, K, V> Table<T, K, V> custom(Map<T, Map<K, V>> table) {
        return new Table<>(table);
    }

    public static <T, K, V> Table<T, K, V> concurrent() {
        return new Table<>(new ConcurrentHashMap<>());
    }

    public static <T, K, V> Table<T, K, V> hashMap() {
        return new Table<>(new HashMap<>());
    }

    private static <K, V> V computeIfAbsent(Map<K, V> map, K key, Lazy<K, V> mappingFunction) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        return map.put(key, mappingFunction.invoke(key));
    }

    private static <K, V> V getOrDefault(Map<K, V> map, K key, V def) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        return def;
    }

    public V put(T type, K name, V value) {
        return computeIfAbsent(table, type, key -> new HashMap<K, V>()).put(name, value);
    }

    public V get(T type, K name) {
        return getOrDefault(table, type, Collections.emptyMap()).get(name);
    }

    public boolean contains(T type, K name) {
        return table.containsKey(type) && table.get(type).containsKey(name);
    }

    public V remove(T type, K name) {
        return getOrDefault(table, type, Collections.emptyMap()).remove(name);
    }

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

    public Collection<V> values() {
        Array<V> values = new Array<>();

        for (Map<K, V> map : table.values()) {
            values.addAll(map.values());
        }

        return values;
    }

    public void clear() {
        table.clear();
    }

    public static class Entry<T, K, V> {
        T type;
        K key;
        V value;

        public Entry(T type, K key, V value) {
            this.type = type;
            this.key = key;
            this.value = value;
        }

        public Entry<T, K, V> setType(T type) {
            this.type = type;
            return this;
        }

        public T getType() {
            return type;
        }

        public Entry<T, K, V> setKey(K key) {
            this.key = key;
            return this;
        }

        public K getKey() {
            return key;
        }

        public Entry<T, K, V> setValue(V value) {
            this.value = value;
            return this;
        }

        public V getValue() {
            return value;
        }

        public boolean equals(T type, K key) {
            return getType() == type && getKey() == key;
        }

        public boolean equals(Entry<T, K, V> entry) {
            return equals(entry.getType(), entry.getKey());
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, key);
        }

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