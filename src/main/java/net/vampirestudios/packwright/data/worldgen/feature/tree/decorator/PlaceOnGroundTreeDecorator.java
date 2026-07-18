package net.vampirestudios.packwright.data.worldgen.feature.tree.decorator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.data.worldgen.feature.BlockStateProvider;

public record PlaceOnGroundTreeDecorator(int tries, int radius, int height, BlockStateProvider provider) implements TreeDecorator {
	public static final MapCodec<PlaceOnGroundTreeDecorator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:place_on_ground"),
			Codec.INT.fieldOf("tries").orElse(128).forGetter(PlaceOnGroundTreeDecorator::tries),
			Codec.INT.fieldOf("radius").orElse(2).forGetter(PlaceOnGroundTreeDecorator::radius),
			Codec.INT.fieldOf("height").orElse(1).forGetter(PlaceOnGroundTreeDecorator::height),
			BlockStateProvider.CODEC.fieldOf("block_state_provider").forGetter(PlaceOnGroundTreeDecorator::provider)
	).apply(i, (type, tries, radius, height, provider) ->
			new PlaceOnGroundTreeDecorator(tries, radius, height, provider)));
}
