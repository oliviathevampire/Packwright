package net.vampirestudios.packwright.data.worldgen.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/** {@code minecraft:random_spread}: a spaced grid of chunks, offset by a random amount per cell */
public record RandomSpreadPlacement(int salt, int spacing, int separation) implements StructurePlacement {
	public static final MapCodec<RandomSpreadPlacement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:random_spread"),
			Codec.INT.fieldOf("salt").forGetter(RandomSpreadPlacement::salt),
			Codec.INT.fieldOf("spacing").forGetter(RandomSpreadPlacement::spacing),
			Codec.INT.fieldOf("separation").forGetter(RandomSpreadPlacement::separation)
	).apply(i, (type, salt, spacing, separation) -> new RandomSpreadPlacement(salt, spacing, separation)));
}
