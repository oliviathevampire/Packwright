package net.vampirestudios.packwright.data.worldgen.feature.tree.decorator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.data.worldgen.feature.BlockStateProvider;

import java.util.List;

public record AttachedToLeavesTreeDecorator(
		float probability,
		int exclusionRadiusXz,
		int exclusionRadiusY,
		BlockStateProvider blockProvider,
		int requiredEmptyBlocks,
		List<String> directions
) implements TreeDecorator {
	public static final MapCodec<AttachedToLeavesTreeDecorator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:attached_to_leaves"),
			Codec.FLOAT.fieldOf("probability").forGetter(AttachedToLeavesTreeDecorator::probability),
			Codec.INT.fieldOf("exclusion_radius_xz").forGetter(AttachedToLeavesTreeDecorator::exclusionRadiusXz),
			Codec.INT.fieldOf("exclusion_radius_y").forGetter(AttachedToLeavesTreeDecorator::exclusionRadiusY),
			BlockStateProvider.CODEC.fieldOf("block_provider").forGetter(AttachedToLeavesTreeDecorator::blockProvider),
			Codec.INT.fieldOf("required_empty_blocks").forGetter(AttachedToLeavesTreeDecorator::requiredEmptyBlocks),
			Codec.STRING.listOf().fieldOf("directions").forGetter(AttachedToLeavesTreeDecorator::directions)
	).apply(i, (type, probability, exclusionRadiusXz, exclusionRadiusY, blockProvider, requiredEmptyBlocks, directions) ->
			new AttachedToLeavesTreeDecorator(probability, exclusionRadiusXz, exclusionRadiusY, blockProvider, requiredEmptyBlocks, directions)));
}
