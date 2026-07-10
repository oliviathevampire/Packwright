package net.vampirestudios.packwright.data.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record HeightProvider(String type, VerticalAnchor minInclusive, VerticalAnchor maxInclusive) {
	public static final Codec<HeightProvider> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(HeightProvider::type),
			VerticalAnchor.CODEC.fieldOf("min_inclusive").forGetter(HeightProvider::minInclusive),
			VerticalAnchor.CODEC.fieldOf("max_inclusive").forGetter(HeightProvider::maxInclusive)
	).apply(i, HeightProvider::new));

	public static HeightProvider uniform(VerticalAnchor min, VerticalAnchor max) {
		return new HeightProvider("minecraft:uniform", min, max);
	}
}