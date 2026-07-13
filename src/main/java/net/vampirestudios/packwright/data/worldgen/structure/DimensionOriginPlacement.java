package net.vampirestudios.packwright.data.worldgen.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/** {@code minecraft:dimension_origin}: a single structure at the dimension origin */
public record DimensionOriginPlacement() implements StructurePlacement {
	public static final MapCodec<DimensionOriginPlacement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:dimension_origin")
	).apply(i, type -> new DimensionOriginPlacement()));
}
