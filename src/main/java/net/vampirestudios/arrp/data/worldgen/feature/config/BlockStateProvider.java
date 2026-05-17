package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import net.vampirestudios.arrp.data.worldgen.BlockState;

public interface BlockStateProvider {
	Codec<BlockStateProvider> CODEC = SimpleStateProvider.CODEC.xmap(provider -> provider, provider -> {
		if (provider instanceof SimpleStateProvider simple) return simple;
		throw new IllegalArgumentException("Unsupported block state provider: " + provider.getClass().getSimpleName());
	});

	static BlockStateProvider simple(String block) {
		return new SimpleStateProvider(BlockState.blockState(block));
	}

	static BlockStateProvider simple(BlockState state) {
		return new SimpleStateProvider(state);
	}
}
