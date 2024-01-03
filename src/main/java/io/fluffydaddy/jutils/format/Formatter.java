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

package io.fluffydaddy.jutils.format;

import java.io.Serializable;

public interface Formatter extends Appendable, CharSequence, Serializable {
    Formatter append(CharSequence text);

    Formatter append(Object obj);

    Formatter append(String text, Object... args);

    Formatter append(CharSequence text, int start, int end);

    Formatter append(char c);

    <T> Formatter append(T[] items);

    Formatter println(CharSequence text, int start, int end);

    Formatter println(String text, Object... args);

    Formatter println(CharSequence text);

    Formatter println(Object obj);

    Formatter println(char c);

    <T> Formatter println(T[] items);

    Formatter println();

    String format(Object... args);

	boolean isEmpty();

	CharSequence subSequence(int start, int end);

    char charAt(int index);

    int length();

    String toString();
}
