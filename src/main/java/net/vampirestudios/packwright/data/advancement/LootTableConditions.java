package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;

import java.util.Optional;

/** conditions for {@code minecraft:player_generates_container_loot} */
public final class LootTableConditions extends CriterionConditions {
	public static final Identifier TYPE = Identifier.withDefaultNamespace("player_generates_container_loot");

	public static final MapCodec<LootTableConditions> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			EntityPredicate.CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
			Identifier.CODEC.fieldOf("loot_table").forGetter(x -> x.lootTable)
	).apply(i, (player, lootTable) -> {
		LootTableConditions out = new LootTableConditions();
		out.player = player.orElse(null);
		out.lootTable = lootTable;
		return out;
	}));

	static {
		CriterionConditions.register(TYPE.toString(), MAP_CODEC.codec());
	}

	private EntityPredicate player;
	private Identifier lootTable;

	public LootTableConditions() {
		super(TYPE.toString());
	}

	public static LootTableConditions lootTableUsed(Identifier lootTable) {
		LootTableConditions out = new LootTableConditions();
		out.lootTable = lootTable;
		return out;
	}

	public LootTableConditions player(EntityPredicate player) {
		this.player = player;
		return this;
	}

	public EntityPredicate getPlayer() { return player; }
	public Identifier getLootTable() { return lootTable; }
}
