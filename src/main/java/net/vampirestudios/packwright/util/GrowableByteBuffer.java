package net.vampirestudios.packwright.util;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.Objects;

/**
 * An unsynchronized, growable byte sink. Does the same job as
 * {@link java.io.ByteArrayOutputStream} without the per-write locking,
 * since packs are only ever written from one thread at a time.
 */
public final class GrowableByteBuffer extends OutputStream {
	private byte[] data;
	private int size;

	public GrowableByteBuffer() {
		this(256);
	}

	public GrowableByteBuffer(int initialCapacity) {
		if (initialCapacity < 0) {
			throw new IllegalArgumentException("initialCapacity must not be negative: " + initialCapacity);
		}
		this.data = new byte[Math.max(initialCapacity, 16)];
	}

	@Override
	public void write(int b) {
		grow(1);
		this.data[this.size++] = (byte) b;
	}

	@Override
	public void write(byte[] src, int off, int len) {
		Objects.checkFromIndexSize(off, len, src.length);
		grow(len);
		System.arraycopy(src, off, this.data, this.size, len);
		this.size += len;
	}

	private void grow(int extra) {
		int needed = this.size + extra;
		if (needed > this.data.length) {
			this.data = Arrays.copyOf(this.data, Math.max(needed, this.data.length * 2));
		}
	}

	/**
	 * @return a copy of everything written so far
	 */
	public byte[] toByteArray() {
		return Arrays.copyOf(this.data, this.size);
	}

	public int size() {
		return this.size;
	}
}
