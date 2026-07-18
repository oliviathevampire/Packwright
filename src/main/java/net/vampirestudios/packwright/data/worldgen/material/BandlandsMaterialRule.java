package net.vampirestudios.packwright.data.worldgen.material;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/** places the badlands terracotta color-band pattern */
public record BandlandsMaterialRule() implements MaterialRule {
	public static final MapCodec<BandlandsMaterialRule> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:bandlands")
	).apply(i, type -> new BandlandsMaterialRule()));
}
