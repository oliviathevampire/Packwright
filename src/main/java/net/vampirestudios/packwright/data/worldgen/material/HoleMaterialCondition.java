package net.vampirestudios.packwright.data.worldgen.material;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/** true if the surface depth at this column is not positive (e.g. cave entrances, overhangs) */
public record HoleMaterialCondition() implements MaterialCondition {
	public static final MapCodec<HoleMaterialCondition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:hole")
	).apply(i, type -> new HoleMaterialCondition()));
}
