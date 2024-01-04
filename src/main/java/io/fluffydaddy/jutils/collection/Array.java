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

import io.fluffydaddy.jutils.reflect.CollectionUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * An extension of {@link ArrayList} implementing {@link Deque}.
 *
 * <p>This class provides additional methods for convenient operations on arrays,
 * lazy evaluations for transformations, and stream-like functionality.</p>
 *
 * <p>Instances of this class can be created using various constructors,
 * and operations can be performed using stream-like methods for filtering,
 * mapping, sorting, and more.</p>
 *
 * <p>Additionally, this class implements the {@link Deque} interface, allowing
 * for double-ended queue operations.</p>
 *
 * <p>This class is licensed under the Apache License, Version 2.0.</p>
 *
 * @param <E> the type of elements in this array
 * @see Deque
 * @see ArrayList
 */
public class Array<E> extends ArrayList<E> implements Deque<E> {
    //region CONSTRUCTORS
    /**
     * A lazy evaluation function that converts an object to its string representation.
     * The default implementation returns "null" if the source object is null, otherwise, it uses the object's
     * {@code toString()} method.
     * <p>
     * This field is intended for use in the {@code joinToString} methods to customize the string representation of elements.
     *
     * @see #joinToString()
     * @see #joinToString(String)
     * @see #joinToString(Lazy, CharSequence, CharSequence, CharSequence)
     * @see #joinToString(Lazy, CharSequence, CharSequence, CharSequence, int, CharSequence)
     */
    public final Lazy<E, String> OBJECT_TO_STRING = source -> source != null ? source.toString() : "null";
    
    /**
     * Constructs an {@code Array} containing the elements of the specified iterable.
     *
     * @param iterable the iterable whose elements are to be placed into this array
     */
    public Array(Iterable<? extends E> iterable) {
        super(listOf(iterable));
    }
    
    /**
     * Constructs an {@code Array} containing the elements of the specified collection.
     *
     * @param collection the collection whose elements are to be placed into this array
     */
    public Array(Collection<? extends E> collection) {
        super(collection);
    }
    
    /**
     * Constructs an {@code Array} containing the elements of the specified array.
     *
     * @param elements the elements to be placed into this array
     */
    @SafeVarargs
    public Array(E... elements) {
        super(listOf(elements));
    }
    
    /**
     * Constructs an {@code Array} with the specified initial capacity.
     *
     * @param elementCount the initial capacity of the array
     */
    public Array(int elementCount) {
        super(elementCount);
    }
    
    /**
     * Constructs an empty {@code Array}.
     */
    public Array() {
        super();
    }
    
    //endregion
    //region STREAM API
    
    /**
     * Provides a {@link Provider} for this {@code Array}.
     *
     * @return a {@link Provider} containing this {@code Array}
     * @see Provider
     */
    public Provider<Array<E>> provide() {
        return new Producer<>(this);
    }
    
    /**
     * Drops the first {@code n} elements from this {@code Array}.
     *
     * @param n the number of elements to drop
     * @return a new {@code Array} containing the remaining elements
     */
    public Array<E> drop(int n) {
        Array<E> dropped = new Array<>(size() - n);
        for (int i = 0; i < n; i++) {
            dropped.add(get(i + n));
        }
        return dropped;
    }
    
    /**
     * Drops the last {@code n} elements from this {@code Array}.
     *
     * @param n the number of elements to drop
     * @return a new {@code Array} containing the remaining elements
     */
    public Array<E> dropLast(int n) {
        Array<E> dropped = new Array<>(size() - n);
        for (int i = n; i >= 0; i--) {
            dropped.add(get(i + n));
        }
        return dropped;
    }
    
    /**
     * Finds the first element in this {@code Array}.
     *
     * @return a {@link Provider} containing the first element, or an empty provider if the array is empty
     * @see Provider
     */
    public Provider<E> findFirst() {
        if (isEmpty()) {
            return Provider.empty();
        }
        
        return Provider.of(get(0));
    }
    
    /**
     * Finds the first element in this {@code Array} that satisfies the given predicate.
     *
     * @param condition the predicate to test elements
     * @return the first matching element, or {@code null} if no elements match the predicate
     */
    public E firstOrNull(Predicate<? super E> condition) {
        for (E element : this) {
            if (condition.test(element)) {
                return element;
            }
            return null;
        }
        return null;
    }
    
    /**
     * Finds the first element in this {@code Array} that satisfies the given predicate.
     *
     * @param predicate the predicate to test elements
     * @return the first matching element, or {@code null} if no elements match the predicate
     */
    public E find(Predicate<? super E> predicate) {
        for (E element : this) {
            if (predicate.test(element)) {
                return element;
            }
        }
        return null;
    }
    
    /**
     * Applies the given action to each element of this {@code Array}.
     *
     * @param action the action to be performed on each element
     * @return this {@code Array} after applying the action
     */
    public Array<E> forEach(Unit<? super E> action) {
        for (E element : this) {
            action.accept(element);
        }
        return this;
    }
    
    /**
     * Returns a new {@code Array} sorted using the provided comparator.
     *
     * @param comparator the comparator to compare elements
     * @return a new sorted {@code Array}
     */
    public Array<E> sortedWith(Comparator<? super E> comparator) {
        Array<E> objects = new Array<>(this);
        objects.sort(comparator);
        return objects;
    }
    
    /**
     * Returns a new {@code Array} containing only the elements that satisfy the given predicate.
     *
     * @param function the predicate to test elements
     * @return a new filtered {@code Array}
     */
    public Array<E> sortedBy(Predicate<? super E> function) {
        Array<E> objects = new Array<>();
        
        for (E it : this) {
            if (function.test(it)) {
                objects.add(it);
            }
        }
        
        return objects;
    }
    
    /**
     * Applies the given transformation to each element of this {@code Array} and adds the results to the provided collection.
     *
     * @param destination the destination collection to add the transformed elements
     * @param transform   the transformation function
     * @param <R>         the type of the result elements
     * @param <C>         the type of the destination collection
     * @return the provided destination collection after adding the transformed elements
     */
    public <R, C extends Collection<R>> C mapTo(C destination, Lazy<? super E, ? extends R> transform) {
        for (E element : this) {
            destination.add(transform.invoke(element));
        }
        return destination;
    }
    
    /**
     * Associates the elements of this {@code Array} by applying a key-value transformation function.
     *
     * @param transform the key-value transformation function
     * @param <K>       the type of the keys
     * @param <V>       the type of the values
     * @return a {@link Map} associating keys with corresponding values
     */
    public <K, V> Map<K, V> associate(Lazy<? super E, ? extends Pair<K, V>> transform) {
        Map<K, V> mapped = new HashMap<>();
        
        for (E it : this) {
            Pair<K, V> pair = transform.invoke(it);
            mapped.put(pair.getFirst(), pair.getSecond());
        }
        
        return mapped;
    }
    
    /**
     * Associates the elements of this {@code Array} by applying a key transformation function.
     *
     * @param transform the key transformation function
     * @param <K>       the type of the keys
     * @return a {@link Map} associating keys with corresponding elements
     */
    public <K> Map<K, E> associateBy(Lazy<? super E, ? extends K> transform) {
        Map<K, E> mapped = new HashMap<>();
        
        for (E element : this) {
            mapped.put(transform.invoke(element), element);
        }
        
        return mapped;
    }
    
    /**
     * Returns a new {@code Array} containing only the elements that satisfy the given predicate.
     *
     * @param predicate the predicate to test elements
     * @return a new filtered {@code Array}
     */
    public Array<E> filter(Predicate<? super E> predicate) {
        Array<E> filter = new Array<>();
        
        for (E element : this) {
            if (predicate.test(element)) {
                filter.add(element);
            }
        }
        
        return filter;
    }
    
    /**
     * Returns a new {@code Array} containing only the elements that do not satisfy the given predicate.
     *
     * @param predicate the predicate to test elements
     * @return a new filtered {@code Array}
     */
    public Array<E> filterNot(Predicate<? super E> predicate) {
        Array<E> filter = new Array<>();
        
        for (E element : this) {
            if (!predicate.test(element)) {
                filter.add(element);
            }
        }
        
        return filter;
    }
    
    /**
     * Applies the given transformation to each element of this {@code Array} and returns the results.
     *
     * @param transform the transformation function
     * @param <R>       the type of the result elements
     * @return a new {@code Array} containing the transformed elements
     */
    public <R> Array<R> map(Lazy<? super E, ? extends R> transform) {
        Array<R> filter = new Array<>();
        
        for (E element : this) {
            filter.add(transform.invoke(element));
        }
        
        return filter;
    }
    
    /**
     * Applies the given transformation to each element of this {@code Array}, and filters out {@code null} results.
     *
     * @param transform the transformation function
     * @param <R>       the type of the result elements
     * @return a new {@code Array} containing the transformed non-null elements
     */
    public <R> Array<R> mapNotNull(Lazy<? super E, ? extends R> transform) {
        Array<R> filter = new Array<>();
        
        for (E element : this) {
            R result = transform.invoke(element);
            if (result != null) {
                filter.add(result);
            }
        }
        
        return filter;
    }
    
    /**
     * Applies the given transformation to each element of this {@code Array}, returning a flattened result.
     *
     * @param function the transformation function
     * @param <R>      the type of the elements in the resulting flattened {@code Array}
     * @return a new {@code Array} containing the flattened results
     */
    public <R> Array<R> flatMap(Lazy<? super E, ? extends Array<? extends R>> function) {
        final Array<R> result = new Array<>();
        
        for (E it : this) {
            Array<? extends R> mapped = function.invoke(it);
            result.addAll(mapped);
        }
        
        return result;
    }
    
    /**
     * Checks if all elements of this {@code Array} match the given predicate.
     *
     * @param predicate the predicate to test elements
     * @return {@code true} if all elements match the predicate, {@code false} otherwise
     */
    public boolean allMatch(Predicate<? super E> predicate) {
        for (E it : this) {
            if (!predicate.test(it)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Checks if any element of this {@code Array} matches the given predicate.
     *
     * @param predicate the predicate to test elements
     * @return {@code true} if any element matches the predicate, {@code false} otherwise
     */
    public boolean anyMatch(Predicate<? super E> predicate) {
        for (E it : this) {
            if (predicate.test(it)) {
                return true;
            }
        }
        
        return false;
    }
    
    //endregion
    //region TRANSFORMER
    
    /**
     * Converts the array into a {@link Set}, removing any duplicate elements.
     *
     * @return A {@link Set} containing the elements of the array.
     * @see java.util.Set
     */
    public Set<E> toSet() {
        return CollectionUtil.mutableSetOf(this);
    }
    
    /**
     * Converts the array into a {@link Map} using the provided transformation function.
     *
     * @param transform The transformation function to derive keys from elements.
     * @param <K>       The type of keys in the resulting map.
     * @return A {@link Map} with elements transformed into keys using the given function.
     * @see java.util.Map
     */
    public <K> Map<K, E> toMap(Lazy<? super E, ? extends K> transform) {
        Map<K, E> map = new HashMap<>();
        
        forEach((Unit<E>) it -> map.put(transform.invoke(it), it));
        
        return map;
    }
    
    //endregion
    //region UTILITIES
    
    /**
     * Swaps elements at the specified indices within the array.
     *
     * @param first  The index of the first element to be swapped.
     * @param second The index of the second element to be swapped.
     * @throws IndexOutOfBoundsException if either index is out of range.
     */
    public void swapIndex(int first, int second) {
        int cnt = size();
        
        if (first < 0 || first >= cnt) throw new IndexOutOfBoundsException(String.valueOf(first));
        if (second < 0 || second >= cnt) throw new IndexOutOfBoundsException(String.valueOf(second));
        
        E firstValue = get(first);
        set(first, get(second));
        set(second, firstValue);
    }
    
    /**
     * Swaps elements with the specified values within the array.
     *
     * @param first  The first element to be swapped.
     * @param second The second element to be swapped.
     */
    public void swapValue(E first, E second) {
        swapIndex(indexOf(first), indexOf(second));
    }
    
    /**
     * Removes all occurrences of the specified elements from the array.
     *
     * @param elements The elements to be removed.
     * @return {@code true} if at least one element was removed, {@code false} otherwise.
     */
    @SafeVarargs
    public final boolean removeAll(E... elements) {
        return removeAll(listOf(elements));
    }
    
    /**
     * Inserts the specified elements at the end of the array.
     *
     * @param elements The elements to be inserted.
     * @return This array after insertion.
     */
    @SafeVarargs
    public final Array<E> insert(E... elements) {
        addAll(listOf(elements));
        return this;
    }
    
    /**
     * Inserts elements from the specified iterable at the end of the array.
     *
     * @param iterable The iterable containing elements to be inserted.
     * @return {@code true} if all elements were successfully inserted, {@code false} otherwise.
     */
    public boolean insert(Iterable<? extends E> iterable) {
        return addAll(listOf(iterable));
    }
    
    /**
     * Inserts elements from the specified map into the array using the provided transformation function.
     *
     * @param m         The map containing keys to be transformed into elements.
     * @param transform The transformation function to derive elements from keys.
     * @param <K>       The type of keys in the map.
     * @return {@code true} if all elements were successfully inserted, {@code false} otherwise.
     */
    public <K> boolean insert(Map<K, E> m, Lazy<K, E> transform) {
        for (Map.Entry<K, E> entry : m.entrySet()) {
            if (!add(transform.invoke(entry.getKey()))) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Inserts elements transformed from the specified values using the provided transformation function.
     *
     * @param transform The transformation function to derive elements from values.
     * @param elements  The values to be transformed and inserted.
     * @param <T>       The type of values in the iterable.
     * @return {@code true} if all elements were successfully inserted, {@code false} otherwise.
     */
    @SafeVarargs
    public final <T> boolean insert(Lazy<T, E> transform, T... elements) {
        for (T element : elements) {
            if (!add(transform.invoke(element))) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Inserts elements transformed from the specified values using the provided transformation function.
     *
     * @param transform The transformation function to derive elements from values.
     * @param elements  The values to be transformed and inserted.
     * @param <T>       The type of values in the collection.
     * @return {@code true} if all elements were successfully inserted, {@code false} otherwise.
     */
    public <T> boolean insert(Lazy<T, E> transform, Collection<T> elements) {
        for (T element : elements) {
            if (!add(transform.invoke(element))) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Replaces occurrences of a specific value within the array or appends the value if not found.
     *
     * @param value The value to be replaced or appended.
     * @return This array after replacement or append operation.
     */
    public Array<E> replace(E value) {
        int index = indexOf(value);
        if (index < 0) {
            add(value);
            return this;
        }
        set(index, value);
        return this;
    }
    
    /**
     * Sets the contents of the array to the specified elements.
     *
     * @param elements The elements to set in the array.
     * @return This array after setting the elements.
     */
    @SafeVarargs
    public final Array<E> set(E... elements) {
        this.clear();
        return this.insert(elements);
    }
    
    /**
     * Sets the contents of the array to elements transformed from the specified values using the provided transformation function.
     *
     * @param transform The transformation function to derive elements from values.
     * @param elements  The values to be transformed and set.
     * @param <T>       The type of values in the iterable.
     * @return {@code true} if all elements were successfully set, {@code false} otherwise.
     */
    @SafeVarargs
    public final <T> boolean set(Lazy<T, E> transform, T... elements) {
        this.clear();
        return this.insert(transform, elements);
    }
    
    /**
     * Sets the contents of the array to elements transformed from the specified values using the provided transformation function.
     *
     * @param transform The transformation function to derive elements from values.
     * @param elements  The values to be transformed and set.
     * @param <T>       The type of values in the collection.
     * @return {@code true} if all elements were successfully set, {@code false} otherwise.
     */
    public final <T> boolean set(Lazy<T, E> transform, Collection<T> elements) {
        this.clear();
        return this.insert(transform, elements);
    }
    
    /**
     * Removes the first element that matches the given condition.
     *
     * @param condition The condition to test elements against.
     * @return {@code true} if an element was removed, {@code false} otherwise.
     */
    public boolean removeIf(Predicate<? super E> condition) {
        for (E element : this) {
            if (condition.test(element)) {
                return remove(element);
            }
        }
        
        return false;
    }
    
    /**
     * Inserts elements from the specified collection at the end of the array.
     *
     * @param c The collection containing elements to be inserted.
     * @return This array after insertion.
     */
    public Array<E> insert(Collection<? extends E> c) {
        addAll(c);
        return this;
    }
    
    //endregion
    //region STRINGER
    
    /**
     * Joins the elements of the array into a single string using their {@code toString()} representation,
     * separated by a comma and space.
     *
     * @return A string representation of the array elements.
     */
    public String joinToString() {
        return joinToString(OBJECT_TO_STRING);
    }
    
    /**
     * Joins the elements of the array into a single string using their {@code toString()} representation,
     * separated by the specified separator.
     *
     * @param separator The separator to be used between elements.
     * @return A string representation of the array elements.
     */
    public String joinToString(String separator) {
        return joinToString(OBJECT_TO_STRING, separator);
    }
    
    /**
     * Joins the elements of the array into a single string using the provided transformation function
     * to convert each element to a string, separated by a comma and space.
     *
     * @param transform The transformation function to convert elements to strings.
     * @return A string representation of the transformed array elements.
     */
    public String joinToString(Lazy<? super E, String> transform) {
        return joinToString(transform, ", ");
    }
    
    /**
     * Joins the elements of the array into a single string using the provided transformation function
     * to convert each element to a string, separated by the specified separator.
     *
     * @param transform The transformation function to convert elements to strings.
     * @param separator The separator to be used between elements.
     * @return A string representation of the transformed array elements.
     */
    public String joinToString(Lazy<? super E, String> transform, CharSequence separator) {
        return joinToString(transform, separator, "");
    }
    
    /**
     * Joins the elements of the array into a single string using the provided transformation function
     * to convert each element to a string, separated by the specified separator, and suffixed by the given suffix.
     *
     * @param transform The transformation function to convert elements to strings.
     * @param separator The separator to be used between elements.
     * @param suffix    The string to be appended to the resulting string.
     * @return A formatted string representation of the transformed array elements.
     */
    public String joinToString(Lazy<? super E, String> transform, CharSequence separator, CharSequence suffix) {
        return joinToString(transform, separator, suffix, suffix);
    }
    
    /**
     * Joins the elements of the array into a single string using the provided transformation function
     * to convert each element to a string, separated by the specified separator, prefix, and postfix.
     *
     * @param transform The transformation function to convert elements to strings.
     * @param separator The separator to be used between elements.
     * @param prefix    The string to be prepended to the resulting string.
     * @param postfix   The string to be appended to the resulting string.
     * @return A formatted string representation of the transformed array elements.
     */
    public String joinToString(Lazy<? super E, String> transform, CharSequence separator, CharSequence prefix, CharSequence postfix) {
        return joinToString(transform, separator, prefix, postfix, -1);
    }
    
    /**
     * Joins the elements of the array into a single string using the provided transformation function
     * to convert each element to a string, separated by the specified separator, prefix, postfix, and a limit
     * on the number of elements to include in the resulting string.
     *
     * @param transform The transformation function to convert elements to strings.
     * @param separator The separator to be used between elements.
     * @param prefix    The string to be prepended to the resulting string.
     * @param postfix   The string to be appended to the resulting string.
     * @param limit     The maximum number of elements to include in the resulting string.
     * @return A formatted string representation of the transformed array elements.
     */
    public String joinToString(Lazy<? super E, String> transform, CharSequence separator, CharSequence prefix, CharSequence postfix, int limit) {
        return joinToString(transform, separator, prefix, postfix, limit, "...");
    }
    
    /**
     * Joins the elements of the array into a single string using the provided transformation function
     * to convert each element to a string, separated by the specified separator, prefix, postfix, and a limit
     * on the number of elements to include in the resulting string. If the number of elements exceeds the limit,
     * a truncated string is appended.
     *
     * @param transform The transformation function to convert elements to strings.
     * @param separator The separator to be used between elements.
     * @param prefix    The string to be prepended to the resulting string.
     * @param postfix   The string to be appended to the resulting string.
     * @param limit     The maximum number of elements to include in the resulting string.
     * @param truncated The string to be appended if the number of elements exceeds the limit.
     * @return A formatted string representation of the transformed array elements.
     */
    public String joinToString(Lazy<? super E, String> transform, CharSequence separator, CharSequence prefix, CharSequence postfix, int limit, CharSequence truncated) {
        StringBuilder builder = new StringBuilder();
        
        builder.append(prefix);
        
        boolean isFirst = true;
        
        for (int i = 0, l = limit > 0 ? limit : size(); i < l; i++) {
            E element = get(i);
            
            if (isFirst) {
                isFirst = false;
            } else {
                builder.append(separator);
            }
            
            if (transform != null) {
                builder.append(transform.invoke(element));
            } else {
                builder.append(element);
            }
        }
        
        if (limit > 0) {
            if (size() - limit > 0) {
                builder.append(truncated);
            }
        }
        
        builder.append(postfix);
        
        return builder.toString();
    }
    
    //endregion
    //region STATIC
    
    /**
     * Converts a map into an array of pairs.
     *
     * @param <K> The type of keys in the map.
     * @param <V> The type of values in the map.
     * @param map The map to convert.
     * @return An array of pairs representing the key-value pairs in the map.
     */
    public static <K, V> Array<Pair<K, V>> toPairs(Map<K, V> map) {
        Array<Pair<K, V>> pairs = new Array<>(map.size());
        
        for (Map.Entry<K, V> entry : map.entrySet()) {
            pairs.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        
        return pairs;
    }
    
    /**
     * Converts a map into an array of pairs.
     *
     * @param <K>      The type of keys in the map.
     * @param <V>      The type of values in the map.
     * @param iterable The map to convert.
     * @return An array of pairs representing the key-value pairs in the map.
     */
    public static <K, V> Array<Pair<K, V>> fromPairs(Map<K, V> iterable) {
        return new Array<>(toPairs(iterable));
    }
    
    /**
     * Creates a new array containing the specified elements.
     *
     * @param <E>       The type of elements in the array.
     * @param transform The elements to be included in the array.
     * @return A new array containing the specified elements.
     */
    @SafeVarargs
    public static <E> Array<E> listOf(E... transform) {
        return CollectionUtil.listOf(transform);
    }
    
    /**
     * Creates a new array containing the elements of the specified iterable.
     *
     * @param <E>       The type of elements in the array.
     * @param transform The iterable whose elements are to be included in the array.
     * @return A new array containing the elements of the specified iterable.
     */
    public static <E> Array<E> listOf(Iterable<E> transform) {
        if (transform instanceof Array) {
            return (Array<E>) transform;
        }
        if (transform instanceof Collection) {
            return new Array<>(((Collection<E>) transform));
        }
        
        Array<E> list = new Array<>();
        
        for (E element : transform) {
            list.add(element);
        }
        
        return list;
    }
    
    /**
     * Joins the elements of a collection into a single string using the specified separator.
     *
     * @param <E>       The type of elements in the collection.
     * @param col       The collection whose elements are to be joined.
     * @param separator The separator to be used between elements.
     * @return A string representation of the joined elements in the collection.
     */
    public static <E> String joinToString(Collection<E> col, String separator) {
        return Array.listOf(col).joinToString(separator);
    }
    
    /**
     * Transforms elements using the specified transformation function.
     *
     * @param <E>      The type of elements in the array.
     * @param <R>      The type of elements in the resulting array.
     * @param elements The elements to be transformed.
     * @param iterable The transformation function.
     * @return A new array containing the transformed elements.
     */
    public static <E, R> Array<R> map(Iterable<? extends E> elements, Lazy<? super E, ? extends R> iterable) {
        return new Array<E>(elements).map(iterable);
    }
    
    /**
     * Transforms elements using the specified transformation function.
     *
     * @param <E>         The type of elements to transform.
     * @param <R>         The type of elements in the resulting array.
     * @param transformer The transformation function.
     * @param elements    The elements to be transformed.
     * @return A new array containing the transformed elements.
     */
    @SafeVarargs
    public static <E, R> Array<R> transform(Lazy<? super E, ? extends R> transformer, E... elements) {
        if (elements == null || elements.length == 0) {
            return new Array<>();
        }
        
        Array<R> result = new Array<>(elements.length);
        
        for (E element : elements) {
            result.add(transformer.invoke(element));
        }
        
        return result;
    }
    
    //endregion
    //region DEQUEUE
    
    /**
     * Inserts the specified element at the beginning of this deque.
     *
     * @param e The element to add.
     */
    @Override
    public void addFirst(E e) {
        add(0, e);
    }
    
    /**
     * Inserts the specified element at the end of this deque.
     *
     * @param e The element to add.
     */
    @Override
    public void addLast(E e) {
        add(size(), e);
    }
    
    /**
     * Inserts the specified element at the front of this deque.
     *
     * @param e The element to add.
     * @return {@code true} if the element was added, {@code false} otherwise.
     */
    @Override
    public boolean offerFirst(E e) {
        int size = size();
        add(0, e);
        return size > 0;
    }
    
    /**
     * Inserts the specified element at the end of this deque.
     *
     * @param e The element to add.
     * @return {@code true} if the element was added, {@code false} otherwise.
     */
    @Override
    public boolean offerLast(E e) {
        int size = size();
        add(size, e);
        return size > 0;
    }
    
    /**
     * Removes and returns the first element of this deque.
     *
     * @return The first element of this deque.
     * @throws NoSuchElementException If this deque is empty.
     */
    @Override
    public E removeFirst() {
        return remove(0);
    }
    
    /**
     * Removes and returns the last element of this deque.
     *
     * @return The last element of this deque.
     * @throws NoSuchElementException If this deque is empty.
     */
    @Override
    public E removeLast() {
        return remove(size() - 1);
    }
    
    /**
     * Removes and returns the first element of this deque, or returns {@code null} if this deque is empty.
     *
     * @return The first element of this deque, or {@code null} if this deque is empty.
     */
    @Override
    public E pollFirst() {
        return size() > 0 ? removeFirst() : null;
    }
    
    /**
     * Removes and returns the last element of this deque, or returns {@code null} if this deque is empty.
     *
     * @return The last element of this deque, or {@code null} if this deque is empty.
     */
    @Override
    public E pollLast() {
        return size() > 0 ? removeLast() : null;
    }
    
    /**
     * Retrieves and returns the first element of this deque.
     *
     * @return The first element of this deque.
     * @throws NoSuchElementException If this deque is empty.
     */
    @Override
    public E getFirst() {
        return get(0);
    }
    
    /**
     * Retrieves and returns the last element of this deque.
     *
     * @return The last element of this deque.
     * @throws NoSuchElementException If this deque is empty.
     */
    @Override
    public E getLast() {
        return get(size() - 1);
    }
    
    /**
     * Retrieves, but does not remove, the first element of this deque.
     *
     * @return The first element of this deque, or {@code null} if this deque is empty.
     */
    @Override
    public E peekFirst() {
        return size() > 0 ? getFirst() : null;
    }
    
    /**
     * Retrieves, but does not remove, the last element of this deque.
     *
     * @return The last element of this deque, or {@code null} if this deque is empty.
     */
    @Override
    public E peekLast() {
        return size() > 0 ? getLast() : null;
    }
    
    /**
     * Removes the first occurrence of the specified element from this deque.
     *
     * @param o The element to be removed.
     * @return {@code true} if the element was removed, {@code false} otherwise.
     */
    @Override
    public boolean removeFirstOccurrence(Object o) {
        int index = size() > 0 ? indexOf(o) : -1;
        return index > 0 && remove(index) != null;
    }
    
    /**
     * Removes the last occurrence of the specified element from this deque.
     *
     * @param o The element to be removed.
     * @return {@code true} if the element was removed, {@code false} otherwise.
     */
    @Override
    public boolean removeLastOccurrence(Object o) {
        int index = size() > 0 ? lastIndexOf(o) : -1;
        return index > 0 && remove(index) != null;
    }
    
    /**
     * Inserts the specified element at the end of this deque.
     *
     * @param e The element to be inserted.
     * @return {@code true} (as specified by {@link Deque#offerLast}).
     */
    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }
    
    /**
     * Removes and returns the first element of this deque.
     *
     * @return The first element of this deque.
     * @throws NoSuchElementException If this deque is empty.
     */
    @Override
    public E remove() {
        return removeFirst();
    }
    
    /**
     * Removes and returns the first element of this deque, or returns {@code null} if this deque is empty.
     *
     * @return The first element of this deque, or {@code null} if this deque is empty.
     */
    @Override
    public E poll() {
        return pollFirst();
    }
    
    /**
     * Retrieves, but does not remove, the first element of this deque.
     *
     * @return The first element of this deque, or {@code null} if this deque is empty.
     */
    @Override
    public E element() {
        return getFirst();
    }
    
    /**
     * Retrieves, but does not remove, the first element of this deque.
     *
     * @return The first element of this deque, or {@code null} if this deque is empty.
     */
    @Override
    public E peek() {
        return peekFirst();
    }
    
    /**
     * Inserts the specified element at the beginning of this deque.
     *
     * @param e The element to be inserted.
     */
    @Override
    public void push(E e) {
        addFirst(e);
    }
    
    /**
     * Removes and returns the first element of this deque.
     *
     * @return The first element of this deque.
     * @throws NoSuchElementException If this deque is empty.
     */
    @Override
    public E pop() {
        return removeFirst();
    }
    
    /**
     * Returns a reverse order iterator over the elements in this deque.
     *
     * @return A reverse order iterator over the elements in this deque.
     */
    @Override
    @NotNull
    public Iterator<E> descendingIterator() {
        return new DescendingIterator<>(this);
    }
    
    //endregion
    //region PRIVATE
    //region METHODS
    
    /**
     * Checks if the given index is within the valid range of indices for a collection of the specified length.
     *
     * @param index  The index to check.
     * @param length The length of the collection.
     * @return {@code true} if the index is valid, {@code false} otherwise.
     */
    private boolean checkIndex(int index, int length) {
        return index >= 0 && index < length;
    }
    
    //endregion
    //region CLASSES
    
    /**
     * A private iterator for traversing the elements in descending order.
     *
     * @param <E> The type of elements in the iterator.
     */
    private static class DescendingIterator<E> implements Iterator<E> {
        private final Array<E> _listArray;
        
        private int _index;
        
        /**
         * Constructs a new DescendingIterator for the given Array.
         *
         * @param listArray The Array to iterate over in descending order.
         */
        private DescendingIterator(Array<E> listArray) {
            _listArray = listArray;
            _index = listArray.size();
        }
        
        /**
         * Returns {@code true} if there is a previous element, {@code false} otherwise.
         *
         * @return {@code true} if there is a previous element, {@code false} otherwise.
         */
        @Override
        public boolean hasNext() {
            return _index >= 0;
        }
        
        /**
         * Returns the previous element and moves the cursor back by one position.
         *
         * @return The previous element.
         */
        @Override
        public E next() {
            return _listArray.get(--_index);
        }
        
        /**
         * Removes the last element returned by this iterator.
         */
        @Override
        public void remove() {
            _listArray.remove(--_index);
        }
    }
    //endregion
    //endregion
}