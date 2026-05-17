package net.vampirestudios.arrp.data.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LootTable {
	public static final Codec<LootTable> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(LootTable::getType),
			Pool.CODEC.listOf().fieldOf("pools").forGetter(LootTable::getPools),
			Identifier.CODEC.optionalFieldOf("random_sequence").forGetter(t -> Optional.ofNullable(t.randomSequence))
	).apply(i, (type, pools, rnd) -> {
		LootTable t = new LootTable(type, pools);
		rnd.ifPresent(t::randomSequence);
		return t;
	}));

	private final String type;
	private List<Pool> pools;
	private Identifier randomSequence; // NEW

	/**
	 * @see LootTable#loot(String)
	 */
	public LootTable(String type) {
		this.type = type;
	}

	public LootTable(String type, List<Pool> pools) {
		this.type = type;
		this.pools = pools;
	}

	public static LootTable loot(String type) {
		return new LootTable(type);
	}

	/** Single-item block drop: one pool, one roll, one item entry. */
	public static LootTable dropping(Identifier item) {
		return loot("minecraft:block")
				.pool(pool().rolls(1).entry(entry().type("minecraft:item").name(item.toString())));
	}

	public static LootTable dropping(String item) {
		return dropping(Identifier.parse(item));
	}

	public static Entry entry() {
		return new Entry();
	}

	/**
	 * @param condition the predicate's condition identifier
	 */
	public static Condition predicate(String condition) {
		return new Condition(condition);
	}

	public static LootFunction function(String function) {
		return new LootFunction(function);
	}

	public static Pool pool() {
		return new Pool();
	}

	public static Roll roll(int min, int max) {
		return new Roll(min, max);
	}

	// --- NEW: builder + getter ---
	public LootTable randomSequence(Identifier id) {
		this.randomSequence = id;
		return this;
	}

	public LootTable randomSequence(String id) {
		return randomSequence(Identifier.tryParse(id));
	}

	public Identifier getRandomSequence() {
		return randomSequence;
	}

	public LootTable pool(Pool pool) {
		if (this.pools == null) {
			this.pools = new ArrayList<>(1);
		}
		this.pools.add(pool);
		return this;
	}

	public String getType() {
		return type;
	}

	public List<Pool> getPools() {
		return pools == null ? List.of() : List.copyOf(pools);
	}
}
