package net.vampirestudios.arrp.json.worldgen.feature.config;

import com.mojang.serialization.Codec;
import net.vampirestudios.arrp.json.worldgen.BlockState;

public class BlockStateConfig implements FeatureConfig {
	public static final Codec<BlockStateConfig> CODEC = BlockState.CODEC.fieldOf("state").xmap(BlockStateConfig::new, x -> x.state).codec();

	private BlockState state = BlockState.blockState("minecraft:packed_ice");

	public BlockStateConfig() {
	}

	public BlockStateConfig(BlockState state) {
		this.state = state;
	}

	public static BlockStateConfig state(String block) {
		return new BlockStateConfig(BlockState.blockState(block));
	}

	public BlockStateConfig state(BlockState state) {
		this.state = state;
		return this;
	}
}
