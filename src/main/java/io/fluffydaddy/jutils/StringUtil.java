package io.fluffydaddy.jutils;

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
