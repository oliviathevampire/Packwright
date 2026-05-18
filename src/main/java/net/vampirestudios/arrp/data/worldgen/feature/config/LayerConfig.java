package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.arrp.data.worldgen.WorldgenBlockState;
import net.vampirestudios.arrp.util.VanillaIds;

public class LayerConfig implements FeatureConfig {
	public static final Codec<LayerConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.INT.fieldOf("height").forGetter(x -> x.height),
			WorldgenBlockState.CODEC.fieldOf("state").forGetter(x -> x.state)
	).apply(i, (height, state) -> new LayerConfig().height(height).state(state)));

	private int height = 1;
	private WorldgenBlockState state = WorldgenBlockState.blockState(VanillaIds.STONE);

	public static LayerConfig layer(int height, Identifier block) {
		return new LayerConfig().height(height).state(WorldgenBlockState.blockState(block));
	}

	public LayerConfig height(int height) {
		this.height = height;
		return this;
	}

	public LayerConfig state(WorldgenBlockState state) {
		this.state = state;
		return this;
	}
}
