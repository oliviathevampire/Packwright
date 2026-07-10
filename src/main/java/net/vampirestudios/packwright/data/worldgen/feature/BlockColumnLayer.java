package net.vampirestudios.packwright.data.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.data.worldgen.IntProvider;

public record BlockColumnLayer(IntProvider height, BlockStateProvider provider) {
	public static final Codec<BlockColumnLayer> CODEC = RecordCodecBuilder.create(i -> i.group(
			IntProvider.CODEC.fieldOf("height").forGetter(BlockColumnLayer::height),
			BlockStateProvider.CODEC.fieldOf("provider").forGetter(BlockColumnLayer::provider)
	).apply(i, BlockColumnLayer::new));
}
