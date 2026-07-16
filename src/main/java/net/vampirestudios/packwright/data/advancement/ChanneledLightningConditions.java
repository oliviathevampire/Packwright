package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** conditions for {@code minecraft:channeled_lightning} */
public final class ChanneledLightningConditions extends CriterionConditions {
	public static final Identifier TYPE = Identifier.withDefaultNamespace("channeled_lightning");

	public static final MapCodec<ChanneledLightningConditions> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			EntityPredicate.CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
			EntityPredicate.CODEC.listOf().optionalFieldOf("victims", List.of()).forGetter(x -> x.victims)
	).apply(i, (player, victims) -> {
		ChanneledLightningConditions out = new ChanneledLightningConditions();
		out.player = player.orElse(null);
		out.victims.addAll(victims);
		return out;
	}));

	static {
		CriterionConditions.register(TYPE.toString(), MAP_CODEC.codec());
	}

	private EntityPredicate player;
	private final List<EntityPredicate> victims = new ArrayList<>();

	public ChanneledLightningConditions() {
		super(TYPE.toString());
	}

	public static ChanneledLightningConditions channeledLightning(EntityPredicate... victims) {
		ChanneledLightningConditions out = new ChanneledLightningConditions();
		if (victims != null) out.victims.addAll(List.of(victims));
		return out;
	}

	public ChanneledLightningConditions player(EntityPredicate player) {
		this.player = player;
		return this;
	}

	public EntityPredicate getPlayer() { return player; }
	public List<EntityPredicate> getVictims() { return List.copyOf(victims); }
}
