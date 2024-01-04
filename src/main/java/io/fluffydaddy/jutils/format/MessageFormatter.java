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

import io.fluffydaddy.jutils.collection.Array;
import io.fluffydaddy.jutils.reflect.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * A formatter implementation for messages.
 */
public class MessageFormatter implements Formatter {
    /**
     * An array of lowercase units used for formatting file sizes.
     * <p>
     * The units include "b" for bytes, "kb" for kilobytes, "mb" for megabytes, "gb" for gigabytes, and "tb" for terabytes.
     * </p>
     */
    public static final String[] LOWER_UNITS = {"b", "kb", "mb", "gb", "tb"};
    /**
     * An array of uppercase units used for formatting file sizes.
     * <p>
     * The units include "B" for bytes, "KB" for kilobytes, "MB" for megabytes, "GB" for gigabytes, and "TB" for terabytes.
     * </p>
     */
    public static final String[] UPPER_UNITS = {"B", "KB", "MB", "GB", "TB"};
    /**
     * A string representing the system-dependent newline sequence.
     * <p>
     * The value of this field is determined by the system property {@code line.separator}. It represents the newline
     * character(s) appropriate for the underlying operating system.
     * </p>
     */
    public static final String NEWLINE = System.lineSeparator();
    
    /**
     * The internal {@code StringBuilder} used for constructing formatted messages.
     * <p>
     * This field holds the mutable string builder instance used internally by {@code MessageFormatter}
     * to append various elements while constructing a formatted message with placeholders.
     * </p>
     * <p>
     * Example usage:
     * <pre>
     * {@code
     * MessageFormatter msg = new MessageFormatter();
     * msg.append("Hello, ")
     *        .append("User: %s", "John Doe")
     *        .append(". Your account balance is: ").currency(1500.75, Currency.getInstance("USD"))
     *        .append(". The %s today");
     *
     * String formattedMessage = msg.format(new Calendar().get(Calendar.YEAR));
     * }
     * </pre>
     * <p>
     * Note: The {@code StringBuilder} is not thread-safe and is intended for internal use in a single-threaded environment.
     * </p>
     *
     * @see MessageFormatter
     * @see StringBuilder
     */
    private final StringBuilder builder;
    
    /**
     * Constructs a MessageFormatter with an initial capacity of 16.
     */
    public MessageFormatter() {
        this(0);
    }
    
    /**
     * Constructs a MessageFormatter with the specified initial capacity.
     *
     * @param length The initial capacity.
     */
    public MessageFormatter(int length) {
        this(new StringBuilder(length));
    }
    
    /**
     * Constructs a MessageFormatter with the specified StringBuilder.
     *
     * @param builder The StringBuilder to use.
     */
    public MessageFormatter(StringBuilder builder) {
        this.builder = builder;
    }
    
    /**
     * Appends the specified character sequence to this formatter.
     *
     * @param text The character sequence to append. If {@code csq} is
     *             {@code null}, then the four characters {@code "null"} are
     *             appended to this Appendable.
     * @return This Formatter.
     */
    @Override
    public MessageFormatter append(CharSequence text) {
        builder.append(text);
        return this;
    }
    
    /**
     * Appends the string representation of the specified object to this formatter.
     *
     * @param obj The object to append. If {@code csq} is
     *            {@code null}, then the four characters {@code "null"} are
     *            appended to this Appendable.
     * @return This Formatter.
     */
    @Override
    public MessageFormatter append(Object obj) {
        builder.append(obj);
        return this;
    }
    
    /**
     * Appends a formatted string to this formatter using the specified arguments.
     *
     * @param text The character sequence to append. If {@code csq} is
     *             {@code null}, then the four characters {@code "null"} are
     *             appended to this Appendable.
     * @param args The arguments of the string format message.
     * @return This Formatter.
     */
    @Override
    public MessageFormatter append(String text, Object... args) {
        return append(String.format(text, args));
    }
    
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
    @Override
    public MessageFormatter append(CharSequence text, int start, int end) {
        builder.append(text, start, end);
        return this;
    }
    
    /**
     * Appends the specified character to this formatter.
     *
     * @param c The character to append.
     * @return This Formatter.
     */
    @Override
    public MessageFormatter append(char c) {
        builder.append(c);
        return this;
    }
    
    /**
     * Appends the string representation of the specified array to this formatter.
     *
     * @param <T>   Any type.
     * @param items Any items to append.
     * @return This Formatter.
     */
    @Override
    public <T> MessageFormatter append(T[] items) {
        builder.append(Array.listOf(items));
        return this;
    }
    
    /**
     * Appends a new line with the specified subsequence to this formatter.
     *
     * @param text  Message text.
     * @param start Start index of the text.
     * @param end   End index of the text.
     * @return This Formatter.
     */
    @Override
    public MessageFormatter println(CharSequence text, int start, int end) {
        return append(text, start, end).append(NEWLINE);
    }
    
    /**
     * Appends a new line with the formatted string to this formatter using the specified arguments.
     *
     * @param text Message text.
     * @param args Format message arguments.
     * @return This Formatter.
     */
    @Override
    public MessageFormatter println(String text, Object... args) {
        return append(text, args).append(NEWLINE);
    }
    
    /**
     * Appends a new line with the specified character sequence to this formatter.
     *
     * @param text Append on a newline.
     * @return This Formatter.
     */
    @Override
    public MessageFormatter println(CharSequence text) {
        return append(text).append(NEWLINE);
    }
    
    /**
     * Appends a new line with the string representation of the specified object to this formatter.
     *
     * @param obj Any item on a newline.
     * @return This Formatter.
     */
    @Override
    public MessageFormatter println(Object obj) {
        return append(obj).append(NEWLINE);
    }
    
    /**
     * Appends a new line with the specified character to this formatter.
     *
     * @param c Character on a newline.
     * @return This Formatter.
     */
    @Override
    public MessageFormatter println(char c) {
        return append(c).append(NEWLINE);
    }
    
    /**
     * Appends a new line with the string representation of the specified array to this formatter.
     *
     * @param <T>   Any type.
     * @param items Any items to append.
     * @return This Formatter.
     */
    @Override
    public <T> MessageFormatter println(T[] items) {
        return append(items).append(NEWLINE);
    }
    
    /**
     * Appends a new line message to this formatter.
     *
     * @return This Formatter.
     */
    @Override
    public MessageFormatter println() {
        return append(NEWLINE);
    }
    
    /**
     * Appends a formatted representation of the specified currency value to this formatter.
     *
     * @param value    The currency value to format.
     * @param currency The currency instance representing the currency of the value.
     * @return A reference to this formatter for chaining additional operations.
     */
    @Override
    public MessageFormatter currency(double value, Currency currency) {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.forLanguageTag(currency.getCurrencyCode()));
        format.setCurrency(currency);
        builder.append(format.format(value));
        return this;
    }
    
    /**
     * Returns a formatted message of this text using the specified arguments.
     *
     * @param args Any items to append for the message.
     * @return The formatted message.
     */
    @Override
    public String format(Object... args) {
        if (args == null) {
            return builder.toString();
        }
        
        return String.format(toString(), args);
    }
    
    /**
     * Returns a subsequence of this formatter.
     *
     * @param start The start index, inclusive.
     * @param end   The end index, exclusive.
     * @return The subsequence.
     */
    @Override
    public @NotNull CharSequence subSequence(int start, int end) {
        return builder.subSequence(start, end);
    }
    
    /**
     * Returns the character at the specified index.
     *
     * @param index The index of the {@code char} value to be returned.
     * @return The character at the specified index.
     */
    @Override
    public char charAt(int index) {
        return builder.charAt(index);
    }
    
    /**
     * Returns the length of the content.
     *
     * @return The content length.
     */
    @Override
    public int length() {
        return builder.length();
    }
    
    /**
     * Returns the string representation of this formatter.
     *
     * @return The string representation.
     */
    @Override
    public @NotNull String toString() {
        return builder.toString();
    }
    
    /**
     * Formats the given size into a human-readable string.
     *
     * @param size    The size to be formatted.
     * @param isLower If true, use lower case units; otherwise, use upper case units.
     * @return The formatted size string.
     */
    public static String formatSize(double size, boolean isLower) {
        return formatSize(size, isLower ? LOWER_UNITS : UPPER_UNITS);
    }
    
    /**
     * Formats the given size into a human-readable string.
     *
     * @param size  The size to be formatted.
     * @param units The array of units.
     * @return The formatted size string.
     */
    public static String formatSize(double size, String[] units) {
        if (size <= 0) return "0";
        
        final int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat(formatSizeOfRegex(2)).format(size / Math.pow(1024, digitGroups)) + units[digitGroups];
    }
    
    private static String formatSizeOfRegex(int regex) {
        return "#,##0." + StringUtil.repeat('#', regex);
    }
}
