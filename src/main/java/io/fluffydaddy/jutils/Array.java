package io.fluffydaddy.jutils;

import java.util.*;

public class Array<E> extends ArrayList<E> implements Deque<E> {
    //region CONSTRUCTORS
    public final Lazy<E, String> OBJECT_TO_STRING = source -> source != null ? source.toString() : "null";

    public Array(Iterable<? extends E> iterable) {
        super(listOf(iterable));
    }

    public Array(Collection<? extends E> collection) {
        super(collection);
    }

    @SafeVarargs
    public Array(E... elements) {
        super(listOf(elements));
    }

    public Array(int elementCount) {
        super(elementCount);
    }

    public Array() {
        super();
    }
    //endregion
    //region STREAM API

    public Array<E> drop(int n) {
        Array<E> dropped = new Array<>(size() - n);
        for (int i = 0; i < n; i++) {
            dropped.add(get(i + n));
        }
        return dropped;
    }

    public Array<E> dropLast(int n) {
        Array<E> dropped = new Array<>(size() - n);
        for (int i = n; i >= 0; i--) {
            dropped.add(get(i + n));
        }
        return dropped;
    }

    public Optional<E> findFirst() {
        if (isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(get(0));
    }

    public E firstOrNull(Predicate<? super E> condition) {
        for (E element : this) {
            if (condition.test(element)) {
                return element;
            }
        }
        return null;
    }

    public E find(Predicate<? super E> iterable) {
        for (E element : this) {
            if (iterable.test(element)) {
                return element;
            }
        }

        return null;
    }

    public Array<E> forEach(Unit<? super E> iterable) {
        for (E element : this) {
            iterable.accept(element);
        }

        return this;
    }

    public Array<E> sortedWith(Comparator<? super E> comparator) {
        Array<E> objects = new Array<>(this);
        objects.sort(comparator);
        return new Array<>(objects);
    }

    public Array<E> sortedBy(Predicate<? super E> function) {
        Array<E> objects = new Array<>();

        for (int i = 0; i < size(); i++) {
            if (function.test(get(i))) {
                swap(i, i + 1);
            }
        }
        return new Array<>(objects);
    }

    public <R, C extends Collection<R>> C mapTo(C destination, Lazy<? super E, ? extends R> transform) {
        for (E element : this) {
            destination.add(transform.invoke(element));
        }
        return destination;
    }

    public <K, V> Map<K, V> associate(Lazy<? super E, ? extends Pair<K, V>> iterable) {
        Map<K, V> mapped = new HashMap<>();

        for (E it : this) {
            Pair<K, V> pair = iterable.invoke(it);
            mapped.put(pair.getFirst(), pair.getSecond());
        }

        return mapped;
    }

    public <K> Map<K, E> associateBy(Lazy<? super E, ? extends K> iterable) {
        Map<K, E> mapped = new HashMap<>();

        for (E element : this) {
            mapped.put(iterable.invoke(element), element);
        }

        return mapped;
    }

    public Array<E> filter(Predicate<? super E> predicate) {
        Array<E> filter = new Array<>();

        for (E element : this) {
            if (predicate.test(element)) {
                filter.add(element);
            }
        }

        return filter;
    }

    public Array<E> filterNot(Predicate<? super E> predicate) {
        Array<E> filter = new Array<>();

        for (E element : this) {
            if (!predicate.test(element)) {
                filter.add(element);
            }
        }

        return filter;
    }

    public <R> Array<R> map(Lazy<? super E, ? extends R> function) {
        Array<R> filter = new Array<>();

        for (E element : this) {
            filter.add(function.invoke(element));
        }

        return filter;
    }

    public <R> Array<R> mapNotNull(Lazy<? super E, ? extends R> function) {
        Array<R> filter = new Array<>();

        for (E element : this) {
            R result = function.invoke(element);
            if (result != null) {
                filter.add(result);
            }
        }

        return filter;
    }

    public <R> Array<R> flatMap(Lazy<? super E, ? extends Array<? extends R>> function) {
        final Array<R> result = new Array<>();

        for (E it : this) {
            Array<? extends R> mapped = function.invoke(it);
            result.addAll(mapped);
        }

        return result;
    }

    public boolean allMatch(Predicate<? super E> predicate) {
        for (E it : this) {
            if (!predicate.test(it)) {
                return false;
            }
        }

        return true;
    }

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
    public Set<E> toSet() {
        return CollectionUtil.mutableSetOf(this);
    }

    public <K> Map<K, E> toMap(Lazy<? super E, ? extends K> transform) {
        Map<K, E> map = new HashMap<>();

        forEach((Unit<E>) it -> map.put(transform.invoke(it), it));

        return map;
    }

    //endregion
    //region UTILITIES
    // Swap Element by index
    public void swap(int first, int second) {
        if (!checkIndex(first, size()) && checkIndex(second, size())) {
            return;
        }

        if (first == second) {
            return;
        }

        E temp = get(first);
        set(indexOf(first), get(second));
        set(indexOf(second), temp);
    }

    // Swap Element by value
    public void swap(E first, E second) {
        swap(indexOf(first), indexOf(second));
    }

    @SafeVarargs
    public final boolean removeAll(E... elements) {
        return removeAll(listOf(elements));
    }

    @SafeVarargs
    public final boolean insert(E... elements) {
        return addAll(listOf(elements));
    }

    public boolean insert(Iterable<? extends E> iterable) {
        return addAll(listOf(iterable));
    }

    public <K> boolean insert(Map<K, E> m, Lazy<K, E> transform) {
        for (Map.Entry<K, E> entry : m.entrySet()) {
            if (!add(transform.invoke(entry.getKey()))) {
                return false;
            }
        }
        return true;
    }

    @SafeVarargs
    public final <T> boolean insert(Lazy<T, E> transform, T... elements) {
        for (T element : elements) {
            if (!add(transform.invoke(element))) {
                return false;
            }
        }

        return true;
    }

    public <T> boolean insert(Lazy<T, E> transform, Collection<T> elements) {
        for (T element : elements) {
            if (!add(transform.invoke(element))) {
                return false;
            }
        }

        return true;
    }

    public Array<E> replace(E value) {
        int index = indexOf(value);
        if (index < 0) {
            add(value);
            return this;
        }
        remove(index);
        if (size() == 0) {
            add(value);
        } else {
            add(index, value);
        }
        return this;
    }

    @SafeVarargs
    public final boolean set(E... elements) {
        this.clear();
        return this.insert(elements);
    }

    @SafeVarargs
    public final <T> boolean set(Lazy<T, E> transform, T... elements) {
        this.clear();
        return this.insert(transform, elements);
    }

    public final <T> boolean set(Lazy<T, E> transform, Collection<T> elements) {
        this.clear();
        return this.insert(transform, elements);
    }

    public boolean removeIf(Predicate<? super E> condition) {
        for (E element : this) {
            if (condition.test(element)) {
                return remove(element);
            }
        }

        return false;
    }

    public Array<E> insert(Collection<? extends E> c) {
        addAll(c);
        return this;
    }

    //endregion
    //region STRINGER
    public String joinToString() {
        return joinToString(OBJECT_TO_STRING);
    }

    public String joinToString(String separator) {
        return joinToString(OBJECT_TO_STRING, separator);
    }

    public String joinToString(Lazy<? super E, String> transform) {
        return joinToString(transform, ", ");
    }

    public String joinToString(Lazy<? super E, String> transform, CharSequence separator) {
        return joinToString(transform, separator, "");
    }

    public String joinToString(Lazy<? super E, String> transform, CharSequence separator, CharSequence suffix) {
        return joinToString(transform, separator, suffix, suffix);
    }

    public String joinToString(Lazy<? super E, String> transform, CharSequence separator, CharSequence prefix, CharSequence postfix) {
        return joinToString(transform, separator, prefix, postfix, -1);
    }

    public String joinToString(Lazy<? super E, String> transform, CharSequence separator, CharSequence prefix, CharSequence postfix, int limit) {
        return joinToString(transform, separator, prefix, postfix, limit, "...");
    }

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
    public static <K, V> Array<Pair<K, V>> toPairs(Map<K, V> map) {
        Array<Pair<K, V>> pairs = new Array<>(map.size());

        for (Map.Entry<K, V> entry : map.entrySet()) {
            pairs.add(new Pair<>(entry.getKey(), entry.getValue()));
        }

        return pairs;
    }

    public static <K, V> Array<Pair<K, V>> fromPairs(Map<K, V> iterable) {
        return new Array<>(toPairs(iterable));
    }

    @SafeVarargs
    public static <E> Array<E> listOf(E... transform) {
        return CollectionUtil.listOf(transform);
    }

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

    public static <E> String joinToString(Collection<E> col, String separator) {
        return Array.listOf(col).joinToString(separator);
    }

    public static <E, R> Array<R> map(Iterable<? extends E> elements, Lazy<? super E, ? extends R> iterable) {
        return new Array<E>(elements).map(iterable);
    }

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
    @Override
    public void addFirst(E e) {
        add(0, e);
    }

    @Override
    public void addLast(E e) {
        add(size(), e);
    }

    @Override
    public boolean offerFirst(E e) {
        int size = size();
        add(0, e);
        return size > 0;
    }

    @Override
    public boolean offerLast(E e) {
        int size = size();
        add(size, e);
        return size > 0;
    }

    @Override
    public E removeFirst() {
        return remove(0);
    }

    @Override
    public E removeLast() {
        return remove(size() - 1);
    }

    @Override
    public E pollFirst() {
        return size() > 0 ? removeFirst() : null;
    }

    @Override
    public E pollLast() {
        return size() > 0 ? removeLast() : null;
    }

    @Override
    public E getFirst() {
        return get(0);
    }

    @Override
    public E getLast() {
        return get(size() - 1);
    }

    @Override
    public E peekFirst() {
        return size() > 0 ? getFirst() : null;
    }

    @Override
    public E peekLast() {
        return size() > 0 ? getLast() : null;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        int index = size() > 0 ? indexOf(o) : -1;
        return index > 0 && remove(index) != null;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        int index = size() > 0 ? lastIndexOf(o) : -1;
        return index > 0 && remove(index) != null;
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new DescendingIterator<>(this);
    }

    //endregion
    //region PRIVATE
    //region METHODS
    private boolean checkIndex(int index, int length) {
        return index >= 0 && index < length;
    }

    //endregion
    //region CLASSES
    private static class DescendingIterator<E> implements Iterator<E> {
        private final Array<E> _listArray;

        private int _index;

        private DescendingIterator(Array<E> listArray) {
            _listArray = listArray;
            _index = listArray.size();
        }

        @Override
        public boolean hasNext() {
            return _index >= 0;
        }

        @Override
        public E next() {
            return _listArray.get(--_index);
        }

        @Override
        public void remove() {
            _listArray.remove(--_index);
        }
    }
    //endregion
    //endregion
}