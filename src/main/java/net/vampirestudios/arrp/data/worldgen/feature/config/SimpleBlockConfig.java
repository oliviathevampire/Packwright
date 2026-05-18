package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.arrp.util.VanillaIds;

public class SimpleBlockConfig implements FeatureConfig {
	public static final Codec<SimpleBlockConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			BlockStateProvider.CODEC.fieldOf("to_place").forGetter(x -> x.toPlace)
	).apply(i, toPlace -> new SimpleBlockConfig().toPlace(toPlace)));

	private BlockStateProvider toPlace = BlockStateProvider.simple(VanillaIds.STONE);

	public static SimpleBlockConfig simpleBlock(Identifier block) {
		return new SimpleBlockConfig().toPlace(BlockStateProvider.simple(block));
	}

	public SimpleBlockConfig toPlace(BlockStateProvider toPlace) {
		this.toPlace = toPlace;
		return this;
	}
}
