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

import java.util.Objects;

/**
 * <p>
 * There are a number of ways to create a {@link Provider} instance. Some common methods:
 * </p>
 *
 * @param <T> Type of value represented by producer
 */
public interface Provider<T> {
    /**
     * Returns the value of this provider if it has a value present, otherwise throws {@code java.lang.IllegalStateException}.
     *
     * @return the current value of this provider.
     * @throws IllegalStateException if there is no value present
     */
    T get();
    
    /**
     * Returns the value of this provider if it has a value present. Returns {@code null} a value is not available.
     *
     * @return the value or {@code null}
     */
    @Nullable
    T getOrNull();
    
    /**
     * Returns the value of this provider if it has a value present. Returns the given default value if a value is
     * not available.
     *
     * @return the value or the default value.
     * @since 4.3
     */
    T getOrElse(T defaultValue);
    
    /**
     * Returns a new {@link Provider} whose value is the value of this producer transformed using the given function.
     *
     * <p>
     * The resulting producer will be live, so that each time it is queried, it queries the original (this) provider
     * and applies the transformation to the result. Whenever the original provider has no value, the new provider
     * will also have no value and the transformation will not be called.
     * </p>
     *
     * <p>
     * When this provider represents a task or the output of a task, the new provider will be considered an output
     * of the task and will carry dependency information that Gradle can use to automatically attach task dependencies
     * to tasks that use the new provider for input values.
     * </p>
     *
     * @param transformer The transformer to apply to values. May return {@code null}, in which case the provider will have no value.
     * @since 4.3
     */
    <S> Provider<S> map(Lazy<? super T, ? extends S> transformer);
    
    /**
     * Returns a new {@link Provider} from the value of this provider transformed using the given function.
     *
     * <p>
     * The new provider returned by {@code flatMap} will be live, so that each time it is queried, it queries
     * this provider and applies the transformation to the result. Whenever this provider has no value, the new
     * provider will also have no value and the transformation will not be called.
     * </p>
     *
     * @param transformer The transformer to apply to values. May return {@code null}, in which case the
     * provider will have no value.
     * @since 5.0
     */
    <S> Provider<S> flatMap(Lazy<? super T, ? extends Provider<? extends S>> transformer);
    
    /**
     * Returns {@code true} if there is a value present, otherwise {@code false}.
     *
     * @return {@code true} if there is a value present, otherwise {@code false}
     */
    boolean isPresent();
    
    /**
     * Returns a {@link Provider} whose value is the value of this provider, if present, otherwise the
     * given default value.
     *
     * @param value The default value to use when this provider has no value.
     */
    Provider<T> orElse(T value);
    
    /**
     * Returns a {@link Provider} whose value is the value of this provider, if present, otherwise uses the
     * value from the given provider, if present.
     *
     * @param provider The provider whose value should be used when this provider has no value.
     */
    Provider<T> orElse(Provider<? extends T> provider);
    
    /**
     * Returns a provider which value will be computed by combining this provider value with another
     * provider value using the supplied combiner function.
     *
     * <p>
     * If the supplied providers represents a task or the output of a task, the resulting provider
     * will carry the dependency information.
     * </p>
     *
     * @param right the second provider to combine with
     * @param combiner the combiner of values
     * @param <B> the type of the second provider
     * @param <R> the type of the result of the combiner
     * @return a combined provider
     *
     * @since 6.6
     */
    <B, R> Provider<R> combine(Provider<B> right, BiFunction<T, B, R> combiner);
    
    /**
     * */
    static <T> Provider<T> empty() {
        return new Producer<>(null);
    }
    
    /**
     * */
    static <T> Provider<T> of(T value) {
        return new Producer<>(Objects.requireNonNull(value));
    }
    
    /**
     * */
    static <T> Provider<T> ofNullable(T value) {
        return value == null ? empty() : new Producer<>(value);
    }
}
