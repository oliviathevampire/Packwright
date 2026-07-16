package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * conditions for {@code minecraft:enter_block} ({@code EnterBlockTrigger}) and
 * {@code minecraft:slide_down_block} ({@code SlideDownBlockTrigger}) — both share the
 * exact same {@code (player, block, state)} shape. Note: {@code minecraft:placed_block}
 * used to live here but that was a bug (it's really {@code ItemUsedOnLocationTrigger}'s
 * {@code (player, location)} shape); see {@link BlockUseConditions}.
 */
public final class PlacedBlockConditions extends CriterionConditions {
	public static final Identifier ENTER_BLOCK = Identifier.withDefaultNamespace("enter_block");
	public static final Identifier SLIDE_DOWN_BLOCK = Identifier.withDefaultNamespace("slide_down_block");

	static {
		CriterionConditions.register(ENTER_BLOCK.toString(), mapCodec(ENTER_BLOCK).codec());
		CriterionConditions.register(SLIDE_DOWN_BLOCK.toString(), mapCodec(SLIDE_DOWN_BLOCK).codec());
	}

	private static MapCodec<PlacedBlockConditions> mapCodec(Identifier trigger) {
		return RecordCodecBuilder.mapCodec(i -> i.group(
				EntityPredicate.CODEC.optionalFieldOf("player").forGetter(x -> Optional.ofNullable(x.player)),
				Identifier.CODEC.optionalFieldOf("block").forGetter(x -> Optional.ofNullable(x.block)),
				Codec.unboundedMap(Codec.STRING, Codec.STRING).optionalFieldOf("state")
						.forGetter(x -> x.state.isEmpty() ? Optional.empty() : Optional.of(x.state))
		).apply(i, (player, block, state) -> {
			PlacedBlockConditions out = new PlacedBlockConditions(trigger);
			out.player = player.orElse(null);
			out.block = block.orElse(null);
			state.ifPresent(out.state::putAll);
			return out;
		}));
	}

	private EntityPredicate player;
	private Identifier block;
	private final Map<String, String> state = new LinkedHashMap<>();

	private PlacedBlockConditions(Identifier trigger) {
		super(trigger.toString());
	}

	public static PlacedBlockConditions entersBlock(Identifier blockId, Map<String, String> state) {
		PlacedBlockConditions out = new PlacedBlockConditions(ENTER_BLOCK);
		out.block = blockId;
		if (state != null) out.state.putAll(state);
		return out;
	}

	public static PlacedBlockConditions slidesDownBlock(Identifier blockId, Map<String, String> state) {
		PlacedBlockConditions out = new PlacedBlockConditions(SLIDE_DOWN_BLOCK);
		out.block = blockId;
		if (state != null) out.state.putAll(state);
		return out;
	}

	public PlacedBlockConditions player(EntityPredicate player) {
		this.player = player;
		return this;
	}

	public EntityPredicate getPlayer() { return player; }
	public Identifier getBlock() { return block; }
	public Map<String, String> getState() { return Map.copyOf(state); }
}
