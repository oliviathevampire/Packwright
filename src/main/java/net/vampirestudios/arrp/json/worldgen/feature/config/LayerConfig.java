package net.vampirestudios.arrp.json.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.json.worldgen.BlockState;

public class LayerConfig implements FeatureConfig {
	public static final Codec<LayerConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.INT.fieldOf("height").forGetter(x -> x.height),
			BlockState.CODEC.fieldOf("state").forGetter(x -> x.state)
	).apply(i, (height, state) -> new LayerConfig().height(height).state(state)));

	private int height = 1;
	private BlockState state = BlockState.blockState("minecraft:stone");

	public static LayerConfig layer(int height, String block) {
		return new LayerConfig().height(height).state(BlockState.blockState(block));
	}

	public LayerConfig height(int height) {
		this.height = height;
		return this;
	}

	public LayerConfig state(BlockState state) {
		this.state = state;
		return this;
	}
}
