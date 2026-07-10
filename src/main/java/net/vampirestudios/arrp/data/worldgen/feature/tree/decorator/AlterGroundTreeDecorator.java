package net.vampirestudios.arrp.data.worldgen.feature.tree.decorator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.data.worldgen.feature.BlockStateProvider;

public record AlterGroundTreeDecorator(BlockStateProvider provider) implements TreeDecorator {
	public static final MapCodec<AlterGroundTreeDecorator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:alter_ground"),
			BlockStateProvider.CODEC.fieldOf("provider").forGetter(AlterGroundTreeDecorator::provider)
	).apply(i, (type, provider) -> new AlterGroundTreeDecorator(provider)));
}
