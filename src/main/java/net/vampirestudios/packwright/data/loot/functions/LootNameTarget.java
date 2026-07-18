package net.vampirestudios.packwright.data.loot.functions;

/**
 * Which text component {@code minecraft:set_name} writes.
 */
public enum LootNameTarget {
	CUSTOM_NAME("custom_name"),
	ITEM_NAME("item_name");

	private final String id;

	LootNameTarget(String id) {
		this.id = id;
	}

	public String id() {
		return this.id;
	}
}
