package net.vampirestudios.arrp.json.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class BlockPileConfig implements FeatureConfig {
	public static final Codec<BlockPileConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			BlockStateProvider.CODEC.fieldOf("state_provider").forGetter(x -> x.stateProvider)
	).apply(i, stateProvider -> new BlockPileConfig().stateProvider(stateProvider)));

	private BlockStateProvider stateProvider = BlockStateProvider.simple("minecraft:hay_block");

	public static BlockPileConfig blockPile(String block) {
		return new BlockPileConfig().stateProvider(BlockStateProvider.simple(block));
	}

	public BlockPileConfig stateProvider(BlockStateProvider stateProvider) {
		this.stateProvider = stateProvider;
		return this;
	}
}
