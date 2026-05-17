package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record TagMatchRuleTest(String tag) implements RuleTest {
	public static final Codec<TagMatchRuleTest> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.fieldOf("predicate_type").forGetter(x -> "minecraft:tag_match"),
			Codec.STRING.fieldOf("tag").forGetter(TagMatchRuleTest::tag)
	).apply(i, (type, tag) -> new TagMatchRuleTest(tag)));
}
