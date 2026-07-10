package net.vampirestudios.packwright.data.worldgen.material;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ConditionalMaterialRule(MaterialCondition condition, MaterialRule thenRun) implements MaterialRule {
	public static final MapCodec<ConditionalMaterialRule> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:condition"),
			MaterialCondition.CODEC.fieldOf("if_true").forGetter(ConditionalMaterialRule::condition),
			MaterialRule.CODEC.fieldOf("then_run").forGetter(ConditionalMaterialRule::thenRun)
	).apply(i, (type, condition, thenRun) -> new ConditionalMaterialRule(condition, thenRun)));
}
