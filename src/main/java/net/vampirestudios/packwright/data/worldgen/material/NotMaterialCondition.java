package net.vampirestudios.packwright.data.worldgen.material;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record NotMaterialCondition(MaterialCondition invert) implements MaterialCondition {
	public static final MapCodec<NotMaterialCondition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:not"),
			MaterialCondition.CODEC.fieldOf("invert").forGetter(NotMaterialCondition::invert)
	).apply(i, (type, invert) -> new NotMaterialCondition(invert)));
}
