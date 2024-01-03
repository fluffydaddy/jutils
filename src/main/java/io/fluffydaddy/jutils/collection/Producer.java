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

import io.fluffydaddy.jutils.collection.function.BiFunction;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Default Implementation of provider.
 *
 * @see Provider <T>
 */
public class Producer<T> implements Provider<T> {
    private final T value;
    
    public Producer(T value) {
        this.value = value;
    }
    
    @Override
    public T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }
    
    @Override
    public @Nullable T getOrNull() {
        return value;
    }
    
    @Override
    public T getOrElse(T defaultValue) {
        return value != null ? value : defaultValue;
    }
    
    @Override
    public <S> Provider<S> map(Lazy<? super T, ? extends S> transformer) {
        Objects.requireNonNull(transformer);
        if (!isPresent()) {
            return Provider.empty();
        } else {
            return Provider.ofNullable(transformer.invoke(value));
        }
    }
    
    @Override
    public <S> Provider<S> flatMap(Lazy<? super T, ? extends Provider<? extends S>> transformer) {
        Objects.requireNonNull(transformer);
        if (!isPresent()) {
            return io.fluffydaddy.jutils.collection.Provider.empty();
        } else {
            @SuppressWarnings("unchecked")
            Provider<S> r = (Provider<S>) transformer.invoke(value);
            return Objects.requireNonNull(r);
        }
    }
    
    @Override
    public boolean isPresent() {
        return value != null;
    }
    
    @Override
    public Provider<T> orElse(T value) {
        if (isPresent()) {
            return new Producer<>(this.value);
        }
        return new Producer<>(value);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Provider<T> orElse(Provider<? extends T> provider) {
        if (isPresent()) {
            return new Producer<>(this.value);
        }
        return (Provider<T>) provider;
    }
    
    @Override
    public <B, R> Provider<R> combine(Provider<B> right, BiFunction<T, B, R> combiner) {
        return new Producer<>(combiner.apply(value, right.get()));
    }
}
