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

package io.fluffydaddy.jutils.reflect;

import io.fluffydaddy.jutils.collection.Array;
import io.fluffydaddy.jutils.collection.Pair;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class CollectionUtil {
	private CollectionUtil() {}
	/*
	 public static <T> Set<T> mutableSetOf(Class<T> classSet) {
	 return mutableSetOf(new HashSet<T>());
	 }

	 public static <T> List<T> mutableListOf(Class<T> classList) {
	 return mutableListOf(new ArrayList<T>());
	 }

	 public static <K, V> Map<K, V> mutableMapOf(Class<K> classKey, Class<V> classValue) {
	 return mutableMapOf(new HashMap<K, V>());
	 }
	 */
	public static <T> Set<T> mutableSetOf(Collection<T> elements) {
		if (elements == null || elements.size() == 0) {
			return new LinkedHashSet<T>();
		}
		LinkedHashSet<T> mutableSet = new LinkedHashSet<T>(elements.size());
		// iterate in elements
		for (T element : elements) {
			mutableSet.add(element);
		}
		return mutableSet;
	}

	public static <T> Set<T> mutableSetOf(T... elements) {
		if (elements == null || elements.length == 0) {
			return new LinkedHashSet<T>();
		}
		LinkedHashSet<T> mutableSet = new LinkedHashSet<T>(elements.length);
		// iterate in elements
		for (T element : elements) {
			mutableSet.add(element);
		}
		return mutableSet;
	}

	public static <T> Array<T> mutableListOf(Collection<T> elements) {
		if (elements == null || elements.size() == 0) {
			return new Array<T>();
		}
		Array<T> mutableList = new Array<T>();
		// iterate in elements
		for (T element : elements) {
			mutableList.add(element);
		}
		return mutableList;
	}

	public static <T> Array<T> mutableListOf(T... elements) {
		if (elements == null || elements.length == 0) {
			return new Array<T>();
		}
		Array<T> mutableList = new Array<T>();
		// iterate in elements
		for (T element : elements) {
			mutableList.add(element);
		}
		return mutableList;
	}

	public static <K, V> Map<K, V> mutableMapOf(Pair<K, V>... elements) {
		if (elements == null || elements.length == 0) {
			return new LinkedHashMap<K, V>();
		}
		LinkedHashMap<K, V> mutableMap = new LinkedHashMap<K, V>(elements.length);
		// iterate in elements
		for (Pair<K, V> element : elements) {
			mutableMap.put(element.getFirst(), element.getSecond());
		}
		return mutableMap;
	}

	public static <K, V> Map<K, V> mutableMapOf(Collection<Pair<K, V>> elements) {
		if (elements == null || elements.size() == 0) {
			return new LinkedHashMap<K, V>();
		}
		LinkedHashMap<K, V> mutableMap = new LinkedHashMap<K, V>(elements.size());
		// iterate in elements
		for (Pair<K, V> element : elements) {
			mutableMap.put(element.getFirst(), element.getSecond());
		}
		return mutableMap;
	}

	public static <K, V> Map<K, V> mutableMapOf(Map<K, V> elements) {
		if (elements == null || elements.size() == 0) {
			return new LinkedHashMap<K, V>();
		}
		LinkedHashMap<K, V> mutableMap = new LinkedHashMap<K, V>(elements.size());
		// iterate in elements
		for (Map.Entry<K, V> element : elements.entrySet()) {
			mutableMap.put(element.getKey(), element.getValue());
		}
		return mutableMap;
	}

	public static <T> Array<T> listOf(T... elements) {
		if (elements == null || elements.length == 0) {
			return new Array<T>();
		}

		Array<T> list = new Array<T>(elements.length);

		for (T element : elements) {
			list.add(element);
		}

		return list;
	}

	public static <T> Set<T> setOf(T... elements) {
		if (elements == null || elements.length == 0) {
			return new HashSet<T>();
		}

		HashSet<T> set = new HashSet<T>(elements.length);

		for (T element : elements) {
			set.add(element);
		}

		return set;
	}
}
