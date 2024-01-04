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

/**
 * <p>
 * The {@code Lazy<T, R>} interface represents a lazy operation that takes an input of type {@code T} and produces
 * a result of type {@code R}. It is similar to the {@code java.util.function.Function} interface.
 * </p>
 *
 * @param <T> The type of the input to the lazy operation
 * @param <R> The type of the result produced by the lazy operation
 */
public interface Lazy<T, R> {
    /**
     * Applies this lazy operation to the given argument.
     *
     * @param t the input argument
     * @return the result of the lazy operation
     */
    R invoke(T t);
}
