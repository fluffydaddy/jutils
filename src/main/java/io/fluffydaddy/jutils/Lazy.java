package io.fluffydaddy.jutils;

public interface Lazy<T, R> {
	R invoke(T obj);
}
