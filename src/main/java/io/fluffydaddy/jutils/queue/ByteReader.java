package io.fluffydaddy.jutils.queue;

import java.io.IOException;
import java.io.InputStream;

public class ByteReader extends InputStream {
	private final ByteQueue queue;
	private final ByteQueueListener listener;

	public ByteReader(ByteQueue queue, ByteQueueListener listener) {
		this.queue = queue;
		this.listener = listener;
	}
	
	@Override
	public int read() throws IOException {
		return read(new byte[0], 0, 0);
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		if (listener != null) {
			listener.onInputUpdate();
		}
		try {
			return queue.read(b, off, len);
		} catch (InterruptedException e) {
			throw new IOException(e.getCause());
		}
	}
}
