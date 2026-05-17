package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.data.worldgen.BlockState;

public record SimpleStateProvider(BlockState state) implements BlockStateProvider {
	public static final Codec<SimpleStateProvider> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:simple_state_provider"),
			BlockState.CODEC.fieldOf("state").forGetter(SimpleStateProvider::state)
	).apply(i, (type, state) -> new SimpleStateProvider(state)));
}
