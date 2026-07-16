package net.vampirestudios.packwright.data.worldgen.material;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/** true if the position is at or above the preliminary (pre-surface-rule) noise surface */
public record AbovePreliminarySurfaceMaterialCondition() implements MaterialCondition {
	public static final MapCodec<AbovePreliminarySurfaceMaterialCondition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:above_preliminary_surface")
	).apply(i, type -> new AbovePreliminarySurfaceMaterialCondition()));
}
