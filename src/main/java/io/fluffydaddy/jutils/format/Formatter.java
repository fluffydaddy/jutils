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

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Currency;

/**
 * A text formatting interface.
 */
public interface Formatter extends Appendable, CharSequence, Serializable {
    
    /**
     * Appends the specified character sequence to this formatter.
     *
     * @param text The character sequence to append. If {@code csq} is
     *             {@code null}, then the four characters {@code "null"} are
     *             appended to this Appendable.
     * @return This Formatter.
     */
    Formatter append(CharSequence text);
    
    /**
     * Appends the string representation of the specified object to this formatter.
     *
     * @param obj The object to append. If {@code csq} is
     *            {@code null}, then the four characters {@code "null"} are
     *            appended to this Appendable.
     * @return This Formatter.
     */
    Formatter append(Object obj);
    
    /**
     * Appends a formatted string to this formatter using the specified arguments.
     *
     * @param text The character sequence to append. If {@code csq} is
     *             {@code null}, then the four characters {@code "null"} are
     *             appended to this Appendable.
     * @param args The arguments of the string format message.
     * @return This Formatter.
     */
    Formatter append(String text, Object... args);
    
    /**
     * Appends a subsequence of the specified character sequence to this formatter.
     *
     * @param text  The character sequence from which a subsequence will be
     *              appended. If {@code csq} is {@code null}, then characters
     *              will be appended as if {@code csq} contained the four
     *              characters {@code "null"}.
     * @param start The index of the first character in the subsequence.
     * @param end   The index of the character following the last character in the
     *              subsequence.
     * @return This Formatter.
     */
    Formatter append(CharSequence text, int start, int end);
    
    /**
     * Appends the specified character to this formatter.
     *
     * @param c The character to append.
     * @return This Formatter.
     */
    Formatter append(char c);
    
    /**
     * Appends the string representation of the specified array to this formatter.
     *
     * @param <T>   Any type.
     * @param items Any items to append.
     * @return This Formatter.
     */
    <T> Formatter append(T[] items);
    
    /**
     * Appends a new line with the specified subsequence to this formatter.
     *
     * @param text  Message text.
     * @param start Start index of the text.
     * @param end   End index of the text.
     * @return This Formatter.
     */
    Formatter println(CharSequence text, int start, int end);
    
    /**
     * Appends a new line with the formatted string to this formatter using the specified arguments.
     *
     * @param text Message text.
     * @param args Format message arguments.
     * @return This Formatter.
     */
    Formatter println(String text, Object... args);
    
    /**
     * Appends a new line with the specified character sequence to this formatter.
     *
     * @param text Append on a newline.
     * @return This Formatter.
     */
    Formatter println(CharSequence text);
    
    /**
     * Appends a new line with the string representation of the specified object to this formatter.
     *
     * @param obj Any item on a newline.
     * @return This Formatter.
     */
    Formatter println(Object obj);
    
    /**
     * Appends a new line with the specified character to this formatter.
     *
     * @param c Character on a newline.
     * @return This Formatter.
     */
    Formatter println(char c);
    
    /**
     * Appends a new line with the string representation of the specified array to this formatter.
     *
     * @param <T>   Any type.
     * @param items Any items to append.
     * @return This Formatter.
     */
    <T> Formatter println(T[] items);
    
    /**
     * Appends a new line message to this formatter.
     *
     * @return This Formatter.
     */
    Formatter println();
    
    /**
     * Appends a formatted representation of the specified currency value to this formatter.
     *
     * @param value    The currency value to format.
     * @param currency The currency instance representing the currency of the value.
     * @return A reference to this formatter for chaining additional operations.
     */
    Formatter currency(double value, Currency currency);
    
    /**
     * Returns a formatted message of this text using the specified arguments.
     *
     * @param args Any items to append for the message.
     * @return The formatted message.
     */
    String format(Object... args);
    
    /**
     * Returns a subsequence of this formatter.
     *
     * @param start The start index, inclusive.
     * @param end   The end index, exclusive.
     * @return The subsequence.
     */
    @NotNull
    CharSequence subSequence(int start, int end);
    
    /**
     * Returns the character at the specified index.
     *
     * @param index The index of the {@code char} value to be returned.
     * @return The character at the specified index.
     */
    char charAt(int index);
    
    /**
     * Returns the length of the content.
     *
     * @return The content length.
     */
    int length();
    
    /**
     * Returns the string representation of this formatter.
     *
     * @return The string representation.
     */
    @NotNull
    String toString();
}