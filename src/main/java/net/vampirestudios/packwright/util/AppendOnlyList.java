package net.vampirestudios.packwright.util;

import java.util.AbstractList;
import java.util.List;

/**
 * A live view of a list that only permits additions. Handed to
 * {@link net.vampirestudios.packwright.api.PackwrightCallback} listeners so they can
 * insert their packs but not remove or reorder anyone else's — removal and mutation
 * of existing entries throw {@link UnsupportedOperationException}.
 */
public final class AppendOnlyList<E> extends AbstractList<E> {
	private final List<E> backing;

	public AppendOnlyList(List<E> backing) {
		this.backing = backing;
	}

	@Override
	public E get(int index) {
		return this.backing.get(index);
	}

	@Override
	public int size() {
		return this.backing.size();
	}

	@Override
	public void add(int index, E element) {
		this.backing.add(index, element);
	}
}
