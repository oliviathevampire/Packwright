package net.vampirestudios.packwright.data.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/** conditions for {@code minecraft:placed_block} and {@code minecraft:enter_block} */
public final class PlacedBlockConditions extends CriterionConditions {
	public static final Identifier PLACED_BLOCK = Identifier.withDefaultNamespace("placed_block");
	public static final Identifier ENTER_BLOCK = Identifier.withDefaultNamespace("enter_block");

	static {
		CriterionConditions.register(PLACED_BLOCK.toString(), mapCodec(PLACED_BLOCK).codec());
		CriterionConditions.register(ENTER_BLOCK.toString(), mapCodec(ENTER_BLOCK).codec());
	}

	private static MapCodec<PlacedBlockConditions> mapCodec(Identifier trigger) {
		return RecordCodecBuilder.mapCodec(i -> i.group(
				Identifier.CODEC.fieldOf("block").forGetter(x -> x.block),
				Codec.unboundedMap(Codec.STRING, Codec.STRING).optionalFieldOf("state")
						.forGetter(x -> x.state.isEmpty() ? Optional.empty() : Optional.of(x.state))
		).apply(i, (block, state) -> {
			PlacedBlockConditions out = new PlacedBlockConditions(trigger);
			out.block = block;
			state.ifPresent(out.state::putAll);
			return out;
		}));
	}

	private Identifier block;
	private final Map<String, String> state = new LinkedHashMap<>();

	private PlacedBlockConditions(Identifier trigger) {
		super(trigger.toString());
	}

	public static PlacedBlockConditions placedBlock(Identifier blockId, Map<String, String> state) {
		PlacedBlockConditions out = new PlacedBlockConditions(PLACED_BLOCK);
		out.block = blockId;
		if (state != null) out.state.putAll(state);
		return out;
	}

	public static PlacedBlockConditions enterBlock(Identifier blockId, Map<String, String> state) {
		PlacedBlockConditions out = new PlacedBlockConditions(ENTER_BLOCK);
		out.block = blockId;
		if (state != null) out.state.putAll(state);
		return out;
	}

	public Identifier getBlock() { return block; }
	public Map<String, String> getState() { return Map.copyOf(state); }
}
