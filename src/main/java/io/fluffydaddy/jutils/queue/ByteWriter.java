package io.fluffydaddy.jutils.queue;

import java.io.IOException;
import java.io.OutputStream;

public class ByteWriter extends OutputStream {
	private final ByteQueue queue;
	
	public ByteWriter(ByteQueue queue) {
		this.queue = queue;
	}
	
	@Override
	public void write(int oneByte) throws IOException {
		write(new byte[]{(byte)oneByte}, 0, 1);
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		try {
			this.queue.write(b, off, len);
		} catch (InterruptedException e) {
			throw new IOException(e.getCause());
		}
	}
}
