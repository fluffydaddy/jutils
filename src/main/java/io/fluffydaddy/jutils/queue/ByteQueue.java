package io.fluffydaddy.jutils.queue;

public class ByteQueue {
    public static final int QUEUE_SIZE = 2 * 1024 * 1024;
    private byte[] mBuffer;
    private int mHead;
    private int mStored;

    public ByteQueue(int size) {
        mBuffer = new byte[size];
    }

    public int getAvailable() {
        synchronized (this) {
            return mStored;
        }
    }

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

    public void reset() {
        mBuffer = new byte[QUEUE_SIZE];
        mHead = 0;
        mStored = 0;
    }
}
