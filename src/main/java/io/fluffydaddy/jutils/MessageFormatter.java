package io.fluffydaddy.jutils;

import java.text.DecimalFormat;

public class MessageFormatter implements Formatter {
    public static final String[] LOWER_UNITS = {"b", "kb", "mb", "gb", "tb"};
    public static final String[] UPPER_UNITS = {"B", "KB", "MB", "GB", "TB"};
    public static final String NEWLINE = System.getProperty("line.separator");

    private final StringBuilder builder;

    public MessageFormatter() {
        this(0);
    }

    public MessageFormatter(int length) {
        this(new StringBuilder(length));
    }

    public MessageFormatter(StringBuilder builder) {
        this.builder = builder;
    }

    @Override
    public MessageFormatter append(CharSequence text) {
        builder.append(text);
        return this;
    }

    @Override
    public MessageFormatter append(Object obj) {
        builder.append(obj);
        return this;
    }

    @Override
    public MessageFormatter append(CharSequence text, int start, int end) {
        builder.append(text, start, end);
        return this;
    }

    @Override
    public MessageFormatter append(char c) {
        builder.append(c);
        return this;
    }

    @Override
    public <T> MessageFormatter append(T[] items) {
        builder.append(Array.listOf(items));
        return this;
    }

    @Override
    public MessageFormatter append(String text, Object... args) {
        return append(String.format(text, args));
    }

    @Override
    public MessageFormatter println(CharSequence text, int start, int end) {
        return append(text, start, end).append(NEWLINE);
    }

    @Override
    public MessageFormatter println(String text, Object... args) {
        return append(text, args).append(NEWLINE);
    }

    @Override
    public MessageFormatter println(CharSequence text) {
        return append(text).append(NEWLINE);
    }

    @Override
    public MessageFormatter println(Object obj) {
        return append(obj).append(NEWLINE);
    }

    @Override
    public MessageFormatter println(char c) {
        return append(c).append(NEWLINE);
    }

    @Override
    public <T> MessageFormatter println(T[] items) {
        return append(items).append(NEWLINE);
    }

    @Override
    public MessageFormatter println() {
        return append(NEWLINE);
    }

    @Override
    public String format(Object... args) {
        if (args == null) {
            return builder.toString();
        }

        return String.format(toString(), args);
    }

    @Override
    public boolean isEmpty() {
        return length() == 0;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return builder.subSequence(start, end);
    }

    @Override
    public char charAt(int index) {
        return builder.charAt(index);
    }

    @Override
    public int length() {
        return builder.length();
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    public static String formatSize(double size) {
        return formatSize(size, false);
    }

    public static String formatSize(double size, boolean isLower) {
        return formatSize(size, isLower ? LOWER_UNITS : UPPER_UNITS);
    }

    public static String formatSize(double size, String[] units) {
        if (size <= 0) return "0";

        final int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat(formatSizeOfRegex(2)).format(size / Math.pow(1024, digitGroups)) + units[digitGroups];
    }

    private static String formatSizeOfRegex(int regex) {
        return "#,##0." + StringUtil.repeat('#', regex);
    }
}
