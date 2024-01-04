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
 * A simple data structure representing a pair of elements, typically a key and a value.
 *
 * @param <K> the type of the first element (key)
 * @param <V> the type of the second element (value)
 */
public class Pair<K, V> {
    
    private final K first;   // The first element of the pair
    private final V second;  // The second element of the pair
    
    /**
     * Constructs a new Pair with the specified first and second elements.
     *
     * @param first  the first element of the pair
     * @param second the second element of the pair
     */
    public Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }
    
    /**
     * Gets the first element (key) of the pair.
     *
     * @return the first element of the pair
     */
    public K getFirst() {
        return first;
    }
    
    /**
     * Gets the second element (value) of the pair.
     *
     * @return the second element of the pair
     */
    public V getSecond() {
        return second;
    }
}
