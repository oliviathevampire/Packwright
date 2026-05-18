package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.vampirestudios.arrp.data.worldgen.WorldgenBlockState;
import net.vampirestudios.arrp.util.VanillaIds;

public class BlockStateConfig implements FeatureConfig {
	public static final Codec<BlockStateConfig> CODEC = WorldgenBlockState.CODEC.fieldOf("state").xmap(BlockStateConfig::new, x -> x.state).codec();

	private WorldgenBlockState state = WorldgenBlockState.blockState(VanillaIds.PACKED_ICE);

	public BlockStateConfig() {
	}

	public BlockStateConfig(WorldgenBlockState state) {
		this.state = state;
	}

	public static BlockStateConfig state(Identifier block) {
		return new BlockStateConfig(WorldgenBlockState.blockState(block));
	}

	public BlockStateConfig state(WorldgenBlockState state) {
		this.state = state;
		return this;
	}
}
