package net.vampirestudios.arrp.json.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JLootTable {
	public static final Codec<JLootTable> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(JLootTable::getType),
			JPool.CODEC.listOf().fieldOf("pools").forGetter(JLootTable::getPools),
			Identifier.CODEC.optionalFieldOf("random_sequence").forGetter(t -> Optional.ofNullable(t.randomSequence))
	).apply(i, (type, pools, rnd) -> {
		JLootTable t = new JLootTable(type, pools);
		rnd.ifPresent(t::randomSequence);
		return t;
	}));

	private final String type;
	private List<JPool> pools;
	private Identifier randomSequence; // NEW

	/**
	 * @see JLootTable#loot(String)
	 */
	public JLootTable(String type) {
		this.type = type;
	}

	public JLootTable(String type, List<JPool> pools) {
		this.type = type;
		this.pools = pools;
	}

	public static JLootTable loot(String type) {
		return new JLootTable(type);
	}

	public static JEntry entry() {
		return new JEntry();
	}

	/**
	 * @param condition the predicate's condition identifier
	 */
	public static JCondition predicate(String condition) {
		return new JCondition(condition);
	}

	public static JFunction function(String function) {
		return new JFunction(function);
	}

	public static JPool pool() {
		return new JPool();
	}

	public static JRoll roll(int min, int max) {
		return new JRoll(min, max);
	}

	// --- NEW: builder + getter ---
	public JLootTable randomSequence(Identifier id) {
		this.randomSequence = id;
		return this;
	}

	public JLootTable randomSequence(String id) {
		return randomSequence(Identifier.tryParse(id));
	}

	public Identifier getRandomSequence() {
		return randomSequence;
	}

	public JLootTable pool(JPool pool) {
		if (this.pools == null) {
			this.pools = new ArrayList<>(1);
		}
		this.pools.add(pool);
		return this;
	}

	public String getType() {
		return type;
	}

	public List<JPool> getPools() {
		return pools;
	}
}
