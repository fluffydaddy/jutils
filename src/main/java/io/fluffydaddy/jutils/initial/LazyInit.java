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

package io.fluffydaddy.jutils.initial;
import io.fluffydaddy.jutils.collection.Array;
import io.fluffydaddy.jutils.collection.Lazy;

import java.util.HashMap;

/**
 * An abstract class providing lazy initialization functionality.
 * <p>
 * This class allows subclasses to perform lazy initialization of their members.
 * It uses the Lazy class to achieve lazy loading, providing a more efficient
 * way to initialize objects when needed.
 *
 * @param <T> The type of the subclass extending LazyInit.
 */
public abstract class LazyInit<T extends LazyInit> {
	
	/**
	 * Abstract method for subclasses to implement lazy initialization logic.
	 *
	 * @param lazyInit The array to store Lazy instances for lazy initialization.
	 */
	public abstract void lazyInit(Array<Lazy<T, ?>> lazyInit);
	
	/**
	 * The array to store Lazy instances for lazy initialization.
	 */
	protected final Array<Lazy<T, ?>> lazyStore;
	
	/**
	 * Constructs a LazyInit object.
	 */
	public LazyInit() {
		lazyInit(lazyStore = new Array<>());
	}
	
	/**
	 * Initializes and invokes all the lazy initialization operations.
	 *
	 * @return A HashMap containing the results of the lazy initialization operations.
	 */
	@SuppressWarnings("unchecked")
	protected HashMap<Lazy<T, ?>, Object> init() {
		HashMap<Lazy<T, ?>, Object> init = new HashMap<>();
		for (Lazy<T, ?> lazyInit : lazyStore) {
			init.put(lazyInit, lazyInit.invoke((T) this));
		}
		return init;
	}
	
	/**
	 * Invokes all the lazy initialization operations without returning the results.
	 */
	@SuppressWarnings("unchecked")
	protected void invokeAll() {
		for (Lazy<T, ?> lazyInit : lazyStore) {
			lazyInit.invoke((T) this);
		}
	}
}