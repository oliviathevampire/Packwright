package net.vampirestudios.packwright.data.worldgen.material;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record SequenceMaterialRule(List<MaterialRule> sequence) implements MaterialRule {
	public static final MapCodec<SequenceMaterialRule> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:sequence"),
			MaterialRule.CODEC.listOf().fieldOf("sequence").forGetter(SequenceMaterialRule::sequence)
	).apply(i, (type, sequence) -> new SequenceMaterialRule(sequence)));
}
