package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class LakeConfig implements FeatureConfig {
	public static final Codec<LakeConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			BlockStateProvider.CODEC.fieldOf("fluid").forGetter(x -> x.fluid),
			BlockStateProvider.CODEC.fieldOf("barrier").forGetter(x -> x.barrier)
	).apply(i, (fluid, barrier) -> new LakeConfig().fluid(fluid).barrier(barrier)));

	private BlockStateProvider fluid = BlockStateProvider.simple(Identifier.withDefaultNamespace("water"));
	private BlockStateProvider barrier = BlockStateProvider.simple(Identifier.withDefaultNamespace("stone"));

	public static LakeConfig lake(Identifier fluid, Identifier barrier) {
		return new LakeConfig().fluid(BlockStateProvider.simple(fluid)).barrier(BlockStateProvider.simple(barrier));
	}

	public LakeConfig fluid(BlockStateProvider fluid) {
		this.fluid = fluid;
		return this;
	}

	public LakeConfig barrier(BlockStateProvider barrier) {
		this.barrier = barrier;
		return this;
	}
}
