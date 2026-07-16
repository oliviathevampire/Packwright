package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;

import java.util.Optional;

/** conditions for {@code minecraft:spear_mobs} */
public final class SpearMobsConditions extends CriterionConditions {
	public static final Identifier TYPE = Identifier.withDefaultNamespace("spear_mobs");

	public static final MapCodec<SpearMobsConditions> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			EntityPredicate.CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
			Codec.INT.optionalFieldOf("count").forGetter(x -> Optional.ofNullable(x.count))
	).apply(i, (player, count) -> {
		SpearMobsConditions out = new SpearMobsConditions();
		out.player = player.orElse(null);
		out.count = count.orElse(null);
		return out;
	}));

	static {
		CriterionConditions.register(TYPE.toString(), MAP_CODEC.codec());
	}

	private EntityPredicate player;
	private Integer count;

	public SpearMobsConditions() {
		super(TYPE.toString());
	}

	public static SpearMobsConditions spearMobs(int requiredCount) {
		SpearMobsConditions out = new SpearMobsConditions();
		out.count = requiredCount;
		return out;
	}

	public SpearMobsConditions player(EntityPredicate player) {
		this.player = player;
		return this;
	}

	public EntityPredicate getPlayer() { return player; }
	public Integer getCount() { return count; }
}
