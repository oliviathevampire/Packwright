package net.vampirestudios.arrp.data.loot;

/**
 * The entity a loot condition or function refers to, relative to the loot context.
 */
public enum EntityTarget {
	THIS("this"),
	ATTACKER("attacker"),
	DIRECT_ATTACKER("direct_attacker"),
	ATTACKING_PLAYER("attacking_player");

	private final String id;

	EntityTarget(String id) {
		this.id = id;
	}

	public String id() {
		return this.id;
	}
}
