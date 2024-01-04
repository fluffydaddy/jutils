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
 * Represents a functional interface that takes an input argument of type T and does not return a result.
 * This interface is similar to {@link java.util.function.Consumer}.
 *
 * @param <T> the type of the input argument
 * @see java.util.function.Consumer
 */
public interface Unit<T> {
	
	/**
	 * Accepts an input argument and performs some operation on it.
	 *
	 * @param it the input argument
	 */
	void accept(T it);
}
