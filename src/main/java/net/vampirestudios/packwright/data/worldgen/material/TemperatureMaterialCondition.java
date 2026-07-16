package net.vampirestudios.packwright.data.worldgen.material;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/** true if the current biome is cold enough for snow at this position */
public record TemperatureMaterialCondition() implements MaterialCondition {
	public static final MapCodec<TemperatureMaterialCondition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:temperature")
	).apply(i, type -> new TemperatureMaterialCondition()));
}
