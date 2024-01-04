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
 * <p>
 * The {@code Producer} class is a default implementation of the {@link Provider} interface. It represents a provider
 * that can produce a value, which may be absent. This implementation provides methods for retrieving and transforming
 * the value it holds.
 * </p>
 *
 * @param <T> Type of value represented by the producer
 */
public class Producer<T> implements Provider<T> {
    private final T value;
    
    /**
     * Constructs a {@code Producer} with the specified value.
     *
     * @param value the value to be used by the provider
     */
    public Producer(T value) {
        this.value = value;
    }
    
    /**
     * Returns the value of this provider if it has a value present, otherwise throws {@code java.util.NoSuchElementException}.
     *
     * @return the current value of this provider.
     * @throws NoSuchElementException if there is no value present
     */
    @Override
    public T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }
    
    /**
     * Returns the value of this provider if it has a value present. Returns {@code null} if a value is not available.
     *
     * @return the value or {@code null}
     */
    @Override
    public @Nullable T getOrNull() {
        return value;
    }
    
    /**
     * Returns the value of this provider if it has a value present. Returns the given default value if a value is
     * not available.
     * @param defaultValue If value is not present.
     * @return the value or the default value.
     */
    @Override
    public T getOrElse(T defaultValue) {
        return value != null ? value : defaultValue;
    }
    
    /**
     * Returns a new {@link Provider} whose value is the value of this provider transformed using the given function.
     *
     * <p>
     * The resulting provider will be live, so that each time it is queried, it queries the original (this) provider
     * and applies the transformation to the result. Whenever the original provider has no value, the new provider
     * will also have no value, and the transformation will not be called.
     * </p>
     *
     * <p>
     * When this provider represents a task or the output of a task, the new provider will be considered an output
     * of the task and will carry dependency information that Gradle can use to automatically attach task dependencies
     * to tasks that use the new provider for input values.
     * </p>
     * @param <S> Transform type.
     * @return Returns a new {@link Provider} whose value is the value of this provider transformed using the given function.
     * @param transformer The transformer to apply to values. May return {@code null}, in which case the provider will have no value.
     */
    @Override
    public <S> Provider<S> map(Lazy<? super T, ? extends S> transformer) {
        Objects.requireNonNull(transformer);
        if (!isPresent()) {
            return Provider.empty();
        } else {
            return Provider.ofNullable(transformer.invoke(value));
        }
    }
    
    /**
     * Returns a new {@link Provider} from the value of this provider transformed using the given function.
     *
     * <p>
     * The new provider returned by {@code flatMap} will be live, so that each time it is queried, it queries
     * this provider and applies the transformation to the result. Whenever this provider has no value, the new
     * provider will also have no value, and the transformation will not be called.
     * </p>
     *
     * @param transformer The transformer to apply to values. May return {@code null}, in which case the
     *                    provider will have no value.
     * @param <S> Transform type.
     * @return Returns a new {@link Provider} from the value of this provider transformed using the given function.
     */
    @Override
    public <S> Provider<S> flatMap(Lazy<? super T, ? extends Provider<? extends S>> transformer) {
        Objects.requireNonNull(transformer);
        if (!isPresent()) {
            return Provider.empty();
        } else {
            @SuppressWarnings("unchecked")
            Provider<S> r = (Provider<S>) transformer.invoke(value);
            return Objects.requireNonNull(r);
        }
    }
    
    /**
     * Returns {@code true} if there is a value present, otherwise {@code false}.
     *
     * @return {@code true} if there is a value present, otherwise {@code false}
     */
    @Override
    public boolean isPresent() {
        return value != null;
    }
    
    /**
     * Returns a {@link Provider} whose value is the value of this provider, if present, otherwise the
     * given default value.
     *
     * @param value The default value to use when this provider has no value.
     */
    @Override
    public Provider<T> orElse(T value) {
        if (isPresent()) {
            return new Producer<>(this.value);
        }
        return new Producer<>(value);
    }
    
    /**
     * Returns a {@link Provider} whose value is the value of this provider, if present, otherwise uses the
     * value from the given provider if present.
     *
     * @param provider The provider whose value should be used when this provider has no value.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Provider<T> orElse(Provider<? extends T> provider) {
        if (isPresent()) {
            return new Producer<>(this.value);
        }
        return (Provider<T>) provider;
    }
    
    /**
     * Returns a provider whose value will be computed by combining this provider value with another
     * provider value using the supplied combiner function.
     *
     * <p>
     * If the supplied providers represent a task or the output of a task, the resulting provider
     * will carry the dependency information.
     * </p>
     *
     * @param right    the second provider to combine with
     * @param combiner the combiner of values
     * @param <B>      the type of the second provider
     * @param <R>      the type of the result of the combiner
     * @return a combined provider
     */
    @Override
    public <B, R> Provider<R> combine(Provider<B> right, BiFunction<T, B, R> combiner) {
        return new Producer<>(combiner.apply(value, right.get()));
    }
}
