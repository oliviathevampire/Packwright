package net.vampirestudios.arrp.data.worldgen.material;

import net.vampirestudios.arrp.data.worldgen.VerticalAnchor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record VerticalGradientMaterialCondition(String randomName, VerticalAnchor trueAtAndBelow, VerticalAnchor falseAtAndAbove) implements MaterialCondition {
	public static final MapCodec<VerticalGradientMaterialCondition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:vertical_gradient"),
			Codec.STRING.fieldOf("random_name").forGetter(VerticalGradientMaterialCondition::randomName),
			VerticalAnchor.CODEC.fieldOf("true_at_and_below").forGetter(VerticalGradientMaterialCondition::trueAtAndBelow),
			VerticalAnchor.CODEC.fieldOf("false_at_and_above").forGetter(VerticalGradientMaterialCondition::falseAtAndAbove)
	).apply(i, (type, randomName, trueAtAndBelow, falseAtAndAbove) ->
			new VerticalGradientMaterialCondition(randomName, trueAtAndBelow, falseAtAndAbove)));
}
