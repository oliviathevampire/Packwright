package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class BlockPileConfig implements FeatureConfig {
	public static final Codec<BlockPileConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			BlockStateProvider.CODEC.fieldOf("state_provider").forGetter(x -> x.stateProvider)
	).apply(i, stateProvider -> new BlockPileConfig().stateProvider(stateProvider)));

	private BlockStateProvider stateProvider = BlockStateProvider.simple(Identifier.withDefaultNamespace("hay_block"));

	public static BlockPileConfig blockPile(Identifier block) {
		return new BlockPileConfig().stateProvider(BlockStateProvider.simple(block));
	}

	public BlockPileConfig stateProvider(BlockStateProvider stateProvider) {
		this.stateProvider = stateProvider;
		return this;
	}
}
