package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.vampirestudios.arrp.data.worldgen.WorldgenBlockState;

public interface BlockStateProvider {
	Codec<BlockStateProvider> CODEC = SimpleStateProvider.CODEC.xmap(provider -> provider, provider -> {
		if (provider instanceof SimpleStateProvider simple) return simple;
		throw new IllegalArgumentException("Unsupported block state provider: " + provider.getClass().getSimpleName());
	});

	static BlockStateProvider simple(Identifier block) {
		return new SimpleStateProvider(WorldgenBlockState.blockState(block));
	}

	static BlockStateProvider simple(WorldgenBlockState state) {
		return new SimpleStateProvider(state);
	}
}
