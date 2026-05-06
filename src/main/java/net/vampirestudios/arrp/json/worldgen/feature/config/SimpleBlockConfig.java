package net.vampirestudios.arrp.json.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class SimpleBlockConfig implements FeatureConfig {
	public static final Codec<SimpleBlockConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			BlockStateProvider.CODEC.fieldOf("to_place").forGetter(x -> x.toPlace)
	).apply(i, toPlace -> new SimpleBlockConfig().toPlace(toPlace)));

	private BlockStateProvider toPlace = BlockStateProvider.simple("minecraft:stone");

	public static SimpleBlockConfig simpleBlock(String block) {
		return new SimpleBlockConfig().toPlace(BlockStateProvider.simple(block));
	}

	public SimpleBlockConfig toPlace(BlockStateProvider toPlace) {
		this.toPlace = toPlace;
		return this;
	}
}
