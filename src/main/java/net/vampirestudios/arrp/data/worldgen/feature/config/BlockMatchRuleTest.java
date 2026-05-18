package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public record BlockMatchRuleTest(Identifier block) implements RuleTest {
	public static final Codec<BlockMatchRuleTest> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.fieldOf("predicate_type").forGetter(x -> "minecraft:block_match"),
			Identifier.CODEC.fieldOf("block").forGetter(BlockMatchRuleTest::block)
	).apply(i, (type, block) -> new BlockMatchRuleTest(block)));
}
