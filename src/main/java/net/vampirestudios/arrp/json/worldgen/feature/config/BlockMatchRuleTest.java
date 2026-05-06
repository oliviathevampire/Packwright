package net.vampirestudios.arrp.json.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record BlockMatchRuleTest(String block) implements RuleTest {
	public static final Codec<BlockMatchRuleTest> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.fieldOf("predicate_type").forGetter(x -> "minecraft:block_match"),
			Codec.STRING.fieldOf("block").forGetter(BlockMatchRuleTest::block)
	).apply(i, (type, block) -> new BlockMatchRuleTest(block)));
}
