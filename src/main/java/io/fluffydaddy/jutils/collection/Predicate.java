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
 * The {@code Predicate} interface represents a predicate (boolean-valued function) of one argument. It is commonly
 * used to define conditions that can be tested against elements of a collection or used in filtering operations.
 * </p>
 *
 * @param <T> The type of the input to the predicate
 */
public interface Predicate<T> {
	/**
	 * Evaluates this predicate on the given argument.
	 *
	 * @param it the input argument
	 * @return {@code true} if the input argument matches the predicate, otherwise {@code false}
	 */
	boolean test(T it);
}
