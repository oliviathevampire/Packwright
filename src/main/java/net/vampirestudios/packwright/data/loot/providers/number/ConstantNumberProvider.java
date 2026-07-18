package net.vampirestudios.packwright.data.loot.providers.number;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ConstantNumberProvider(float amount) implements NumberProvider {
	public static final MapCodec<ConstantNumberProvider> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:constant"),
			Codec.FLOAT.fieldOf("value").forGetter(ConstantNumberProvider::amount)
	).apply(i, (type, amount) -> new ConstantNumberProvider(amount)));
}
