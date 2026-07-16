package net.vampirestudios.packwright.data.loot;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * How to merge a new list into an existing one — used by {@code minecraft:set_lore},
 * {@code minecraft:set_written_book_pages}, {@code minecraft:set_writable_book_pages}, and the
 * per-field lists of {@code minecraft:set_custom_model_data}. Vanilla's {@code mode} field (and its
 * {@code offset}/{@code size} companions) has no default, so one of these must always be supplied.
 */
public final class ListOperation {
	private final Map<String, Object> value;

	private ListOperation(Map<String, Object> value) {
		this.value = value;
	}

	private static Map<String, Object> base(String mode) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("mode", mode);
		return map;
	}

	/**
	 * discards the existing list entirely
	 */
	public static ListOperation replaceAll() {
		return new ListOperation(base("replace_all"));
	}

	/**
	 * adds the new entries after the existing ones
	 */
	public static ListOperation append() {
		return new ListOperation(base("append"));
	}

	/**
	 * inserts the new entries at {@code offset}, pushing the rest back
	 */
	public static ListOperation insert(int offset) {
		Map<String, Object> map = base("insert");
		map.put("offset", offset);
		return new ListOperation(map);
	}

	/**
	 * replaces the section of the existing list starting at {@code offset}, sized to the new entries
	 */
	public static ListOperation replaceSection(int offset) {
		return replaceSection(offset, null);
	}

	/**
	 * replaces exactly {@code size} entries of the existing list starting at {@code offset}
	 */
	public static ListOperation replaceSection(int offset, Integer size) {
		Map<String, Object> map = base("replace_section");
		map.put("offset", offset);
		if (size != null) {
			map.put("size", size);
		}
		return new ListOperation(map);
	}

	/**
	 * @return the plain Java map backing this operation ({@code mode} plus its own fields)
	 */
	public Map<String, Object> value() {
		return this.value;
	}
}
