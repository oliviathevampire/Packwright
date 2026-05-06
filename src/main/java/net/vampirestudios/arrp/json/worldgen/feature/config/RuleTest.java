package net.vampirestudios.arrp.json.worldgen.feature.config;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

public interface RuleTest {
	Codec<RuleTest> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<RuleTest, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> {
				String type = FeatureConfigUtil.string(map, ops, "predicate_type", "");
				return switch (FeatureConfigUtil.normalizeType(type)) {
					case "tag_match" -> TagMatchRuleTest.CODEC.decode(ops, input).map(pair -> pair.mapFirst(x -> x));
					case "block_match" -> BlockMatchRuleTest.CODEC.decode(ops, input).map(pair -> pair.mapFirst(x -> x));
					default -> DataResult.error(() -> "Unsupported rule test type: " + type);
				};
			});
		}

		@Override
		public <T> DataResult<T> encode(RuleTest ruleTest, DynamicOps<T> ops, T prefix) {
			if (ruleTest instanceof TagMatchRuleTest tagMatch) return TagMatchRuleTest.CODEC.encode(tagMatch, ops, prefix);
			if (ruleTest instanceof BlockMatchRuleTest blockMatch) return BlockMatchRuleTest.CODEC.encode(blockMatch, ops, prefix);
			return DataResult.error(() -> "Unsupported rule test: " + ruleTest.getClass().getSimpleName());
		}
	};

	static RuleTest tag(String tag) {
		return new TagMatchRuleTest(FeatureConfigUtil.stripTagPrefix(tag));
	}

	static RuleTest block(String block) {
		return new BlockMatchRuleTest(block);
	}
}
