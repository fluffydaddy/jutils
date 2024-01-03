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

import java.util.Arrays;

public class StringUtil {
    public static String substringAfter(String input, String delimiter) {
        int index = input.indexOf(delimiter);
        if (index != -1) {
            return input.substring(index + delimiter.length());
        }
        return "";
    }

    public static String substringBefore(String input, String delimiter) {
        int index = input.indexOf(delimiter);
        if (index != -1) {
            return input.substring(0, index);
        }
        return input;
    }

    public static String repeat(char c, int count) {
        if (count <= 0) return String.valueOf(c);

        char[] result = new char[count];

        Arrays.fill(result, c);

        return new String(result);
    }

    public static String repeat(CharSequence c, int count) {
        if (count <= 0) return String.valueOf(c);

        int len = c.length();
        char[] result = new char[len * count];

        for (int i = 0, j = 0; i < result.length; i++, j++) {
            result[i] = c.charAt(j);
            if (j == len) j = 0; // optimize logic.
        }

        return new String(result);
    }
}
