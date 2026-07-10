package net.vampirestudios.packwright.data.worldgen.feature.builders.geode;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record GeodeLayers(double filling, double innerLayer, double middleLayer, double outerLayer) {
	public static final Codec<GeodeLayers> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.DOUBLE.fieldOf("filling").forGetter(GeodeLayers::filling),
			Codec.DOUBLE.fieldOf("inner_layer").forGetter(GeodeLayers::innerLayer),
			Codec.DOUBLE.fieldOf("middle_layer").forGetter(GeodeLayers::middleLayer),
			Codec.DOUBLE.fieldOf("outer_layer").forGetter(GeodeLayers::outerLayer)
	).apply(i, GeodeLayers::new));
}
