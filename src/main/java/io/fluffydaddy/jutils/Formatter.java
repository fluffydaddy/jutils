package io.fluffydaddy.jutils;

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
