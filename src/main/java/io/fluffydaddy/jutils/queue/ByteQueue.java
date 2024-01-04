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

/**
 * A class for processing bytes in a queue for input, such as a console.
 * <p>
 * This class provides a simple byte queue implementation that can be used
 * for handling input and output operations with byte data. </p>
 */
public class ByteQueue {
    /**
     * The default size of the queue in bytes.
     */
    public static final int QUEUE_SIZE = 2 * 1024 * 1024;
    
    /**
     * The byte buffer used for the queue.
     */
    private byte[] mBuffer;
    
    /**
     * The head index of the queue.
     */
    private int mHead;
    
    /**
     * The number of stored bytes in the queue.
     */
    private int mStored;
    
    /**
     * Constructs a ByteQueue with the specified size.
     *
     * @param size The size of the byte queue.
     */
    public ByteQueue(int size) {
        mBuffer = new byte[size];
    }
    
    /**
     * Gets the number of available bytes in the queue.
     *
     * @return The number of available bytes.
     */
    public int getAvailable() {
        synchronized (this) {
            return mStored;
        }
    }
    
    /**
     * Reads bytes from the queue into the provided buffer.
     *
     * @param buffer The destination buffer.
     * @param offset The starting offset in the destination buffer.
     * @param length The maximum number of bytes to read.
     * @return The actual number of bytes read.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public int read(byte[] buffer, int offset, int length) throws InterruptedException {
        if (length + offset > buffer.length) {
            throw new IllegalArgumentException("length + offset > buffer.length");
        }
        if (length < 0) {
            throw new IllegalArgumentException("length < 0");
        }
        if (length == 0) {
            return 0;
        }
        synchronized (this) {
            while (mStored == 0) {
                wait();
            }
            int totalRead = 0;
            int bufferLength = mBuffer.length;
            boolean wasFull = bufferLength == mStored;
            while (length > 0 && mStored > 0) {
                int oneRun = Math.min(bufferLength - mHead, mStored);
                int bytesToCopy = Math.min(length, oneRun);
                System.arraycopy(mBuffer, mHead, buffer, offset, bytesToCopy);
                mHead += bytesToCopy;
                if (mHead >= bufferLength) {
                    mHead = 0;
                }
                mStored -= bytesToCopy;
                length -= bytesToCopy;
                offset += bytesToCopy;
                totalRead += bytesToCopy;
            }
            if (wasFull) {
                notify();
            }
            return totalRead;
        }
    }
    
    /**
     * Writes bytes from the provided buffer into the queue.
     *
     * @param buffer The source buffer.
     * @param offset The starting offset in the source buffer.
     * @param length The number of bytes to write.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public void write(byte[] buffer, int offset, int length) throws InterruptedException {
        if (length + offset > buffer.length) {
            throw new IllegalArgumentException("length + offset > buffer.length");
        }
        if (length < 0) {
            throw new IllegalArgumentException("length < 0");
        }
        if (length == 0) {
            return;
        }
        synchronized (this) {
            int bufferLength = mBuffer.length;
            boolean wasEmpty = mStored == 0;
            while (length > 0) {
                while (bufferLength == mStored) {
                    wait();
                }
                int tail = mHead + mStored;
                int oneRun;
                if (tail >= bufferLength) {
                    tail = tail - bufferLength;
                    oneRun = mHead - tail;
                } else {
                    oneRun = bufferLength - tail;
                }
                int bytesToCopy = Math.min(oneRun, length);
                System.arraycopy(buffer, offset, mBuffer, tail, bytesToCopy);
                offset += bytesToCopy;
                mStored += bytesToCopy;
                length -= bytesToCopy;
            }
            if (wasEmpty) {
                notify();
            }
        }
    }
    
    /**
     * Resets the queue by creating a new byte buffer and setting head and stored
     * indices to their initial values.
     */
    public void reset() {
        mBuffer = new byte[QUEUE_SIZE];
        mHead = 0;
        mStored = 0;
    }
}