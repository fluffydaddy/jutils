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
