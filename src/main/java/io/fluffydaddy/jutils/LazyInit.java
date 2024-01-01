package io.fluffydaddy.jutils;

import java.util.HashMap;

public abstract class LazyInit<T extends LazyInit> {
	public abstract void lazyInit(Array<Lazy<T, ?>> lazyInit);
	
	protected final Array<Lazy<T, ?>> lazyInits;
	
	public LazyInit() {
		lazyInit(lazyInits = new Array<>());
	}
	
	protected java.util.HashMap<Lazy<T, ?>, Object> init() {
		HashMap<Lazy<T, ?>, Object> init = new HashMap<>();
		for (Lazy<T, ?> lazyInit : lazyInits) {
			init.put(lazyInit, lazyInit.invoke((T)this));
		}
		return init;
	}
	
	protected void invokeAll() {
		for (Lazy<T, ?> lazyInit : lazyInits) {
			lazyInit.invoke((T)this);
		}
	}
}
