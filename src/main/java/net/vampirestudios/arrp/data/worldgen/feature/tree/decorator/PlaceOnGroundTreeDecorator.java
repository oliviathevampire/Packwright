package net.vampirestudios.arrp.data.worldgen.feature.tree.decorator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.data.worldgen.feature.BlockStateProvider;

public record PlaceOnGroundTreeDecorator(int tries, int radius, int height, BlockStateProvider provider) implements TreeDecorator {
	public static final MapCodec<PlaceOnGroundTreeDecorator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:place_on_ground"),
			Codec.INT.fieldOf("tries").forGetter(PlaceOnGroundTreeDecorator::tries),
			Codec.INT.fieldOf("radius").forGetter(PlaceOnGroundTreeDecorator::radius),
			Codec.INT.fieldOf("height").forGetter(PlaceOnGroundTreeDecorator::height),
			BlockStateProvider.CODEC.fieldOf("provider").forGetter(PlaceOnGroundTreeDecorator::provider)
	).apply(i, (type, tries, radius, height, provider) ->
			new PlaceOnGroundTreeDecorator(tries, radius, height, provider)));
}
