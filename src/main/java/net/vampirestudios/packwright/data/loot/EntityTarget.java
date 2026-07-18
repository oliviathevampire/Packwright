package net.vampirestudios.packwright.data.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import java.util.Arrays;

/**
 * The entity a loot condition or function refers to, relative to the loot context.
 */
public enum EntityTarget {
	THIS("this"),
	ATTACKER("attacker"),
	DIRECT_ATTACKER("direct_attacker"),
	ATTACKING_PLAYER("attacking_player");

	public static final Codec<EntityTarget> CODEC = Codec.STRING.comapFlatMap(
			id -> Arrays.stream(values())
					.filter(target -> target.id.equals(id))
					.findFirst()
					.map(DataResult::success)
					.orElseGet(() -> DataResult.error(() -> "Unknown entity target: " + id)),
			EntityTarget::id
	);

	private final String id;

	EntityTarget(String id) {
		this.id = id;
	}

	public String id() {
		return this.id;
	}
}
