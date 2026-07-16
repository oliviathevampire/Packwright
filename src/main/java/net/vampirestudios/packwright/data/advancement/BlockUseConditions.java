package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.loot.Condition;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * conditions for {@code minecraft:placed_block}, {@code minecraft:item_used_on_block} and
 * {@code minecraft:allay_drop_item_on_block} (all backed by {@code ItemUsedOnLocationTrigger}),
 * plus {@code minecraft:default_block_use} ({@code DefaultBlockInteractionTrigger}) and
 * {@code minecraft:any_block_use} ({@code AnyBlockInteractionTrigger}) — all five share the
 * identical {@code (player, location)} shape even though they're backed by different vanilla
 * trigger classes. "location" is a context-aware predicate: a list of loot-condition-style
 * objects (e.g. {@code minecraft:location_check}, {@code minecraft:match_tool}), reusing this
 * project's existing {@link Condition} loot-condition type.
 */
public final class BlockUseConditions extends CriterionConditions {
	public static final Identifier PLACED_BLOCK = Identifier.withDefaultNamespace("placed_block");
	public static final Identifier ITEM_USED_ON_BLOCK = Identifier.withDefaultNamespace("item_used_on_block");
	public static final Identifier ALLAY_DROP_ITEM_ON_BLOCK = Identifier.withDefaultNamespace("allay_drop_item_on_block");
	public static final Identifier DEFAULT_BLOCK_USE = Identifier.withDefaultNamespace("default_block_use");
	public static final Identifier ANY_BLOCK_USE = Identifier.withDefaultNamespace("any_block_use");

	static {
		CriterionConditions.register(PLACED_BLOCK.toString(), mapCodec(PLACED_BLOCK).codec());
		CriterionConditions.register(ITEM_USED_ON_BLOCK.toString(), mapCodec(ITEM_USED_ON_BLOCK).codec());
		CriterionConditions.register(ALLAY_DROP_ITEM_ON_BLOCK.toString(), mapCodec(ALLAY_DROP_ITEM_ON_BLOCK).codec());
		CriterionConditions.register(DEFAULT_BLOCK_USE.toString(), mapCodec(DEFAULT_BLOCK_USE).codec());
		CriterionConditions.register(ANY_BLOCK_USE.toString(), mapCodec(ANY_BLOCK_USE).codec());
	}

	private static MapCodec<BlockUseConditions> mapCodec(Identifier trigger) {
		return RecordCodecBuilder.mapCodec(i -> i.group(
				EntityPredicate.CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
				Condition.CODEC.listOf().optionalFieldOf("location")
						.forGetter(x -> x.location.isEmpty() ? Optional.empty() : Optional.of(x.location))
		).apply(i, (player, location) -> {
			BlockUseConditions out = new BlockUseConditions(trigger);
			out.player = player.orElse(null);
			location.ifPresent(out.location::addAll);
			return out;
		}));
	}

	private EntityPredicate player;
	private final List<Condition> location = new ArrayList<>();

	private BlockUseConditions(Identifier trigger) {
		super(trigger.toString());
	}

	public static BlockUseConditions placedBlock(Condition... location) {
		BlockUseConditions out = new BlockUseConditions(PLACED_BLOCK);
		if (location != null) out.location.addAll(List.of(location));
		return out;
	}

	public static BlockUseConditions itemUsedOnBlock(Condition... location) {
		BlockUseConditions out = new BlockUseConditions(ITEM_USED_ON_BLOCK);
		if (location != null) out.location.addAll(List.of(location));
		return out;
	}

	public static BlockUseConditions allayDropItemOnBlock(Condition... location) {
		BlockUseConditions out = new BlockUseConditions(ALLAY_DROP_ITEM_ON_BLOCK);
		if (location != null) out.location.addAll(List.of(location));
		return out;
	}

	public static BlockUseConditions defaultBlockUse() {
		return new BlockUseConditions(DEFAULT_BLOCK_USE);
	}

	public static BlockUseConditions anyBlockUse() {
		return new BlockUseConditions(ANY_BLOCK_USE);
	}

	public BlockUseConditions player(EntityPredicate player) {
		this.player = player;
		return this;
	}

	public EntityPredicate getPlayer() { return player; }
	public List<Condition> getLocation() { return List.copyOf(location); }
}
