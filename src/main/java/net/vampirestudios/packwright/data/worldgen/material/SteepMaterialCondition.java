package net.vampirestudios.packwright.data.worldgen.material;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/** true if the terrain height changes sharply between neighboring columns */
public record SteepMaterialCondition() implements MaterialCondition {
	public static final MapCodec<SteepMaterialCondition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:steep")
	).apply(i, type -> new SteepMaterialCondition()));
}
