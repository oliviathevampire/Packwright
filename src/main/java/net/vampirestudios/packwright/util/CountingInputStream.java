package net.vampirestudios.packwright.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * An input stream that remembers how many bytes passed through it, used to size
 * output buffers when transforming resources of roughly-known size.
 */
public final class CountingInputStream extends FilterInputStream {
	private long count;

	public CountingInputStream(InputStream in) {
		super(in);
	}

	@Override
	public int read() throws IOException {
		int b = this.in.read();
		if (b >= 0) this.count++;
		return b;
	}

	@Override
	public int read(byte[] buffer, int off, int len) throws IOException {
		int read = this.in.read(buffer, off, len);
		if (read > 0) this.count += read;
		return read;
	}

	@Override
	public long skip(long n) throws IOException {
		long skipped = this.in.skip(n);
		this.count += skipped;
		return skipped;
	}

	/**
	 * @return the number of bytes read so far, clamped to an int
	 */
	public int bytes() {
		return (int) Math.min(this.count, Integer.MAX_VALUE);
	}
}
