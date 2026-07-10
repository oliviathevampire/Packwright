package net.vampirestudios.packwright.data.worldgen.feature.builders.geode;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.data.worldgen.feature.BlockStateProvider;

import java.util.List;

public record GeodeBlocks(
		BlockStateProvider fillingProvider,
		BlockStateProvider innerLayerProvider,
		BlockStateProvider alternateInnerLayerProvider,
		BlockStateProvider middleLayerProvider,
		BlockStateProvider outerLayerProvider,
		List<BlockStateProvider> innerPlacements
) {
	public static final Codec<GeodeBlocks> CODEC = RecordCodecBuilder.create(i -> i.group(
			BlockStateProvider.CODEC.fieldOf("filling_provider").forGetter(GeodeBlocks::fillingProvider),
			BlockStateProvider.CODEC.fieldOf("inner_layer_provider").forGetter(GeodeBlocks::innerLayerProvider),
			BlockStateProvider.CODEC.fieldOf("alternate_inner_layer_provider").forGetter(GeodeBlocks::alternateInnerLayerProvider),
			BlockStateProvider.CODEC.fieldOf("middle_layer_provider").forGetter(GeodeBlocks::middleLayerProvider),
			BlockStateProvider.CODEC.fieldOf("outer_layer_provider").forGetter(GeodeBlocks::outerLayerProvider),
			BlockStateProvider.CODEC.listOf().fieldOf("inner_placements").forGetter(GeodeBlocks::innerPlacements)
	).apply(i, GeodeBlocks::new));
}
