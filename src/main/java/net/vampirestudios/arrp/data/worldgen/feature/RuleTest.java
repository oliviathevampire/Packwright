package net.vampirestudios.arrp.data.worldgen.feature;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.List;

public sealed interface RuleTest permits RuleTest.BlockMatch, RuleTest.TagMatch, RuleTest.HeightMatch, RuleTest.AllOf {
	Codec<RuleTest> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<RuleTest, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> {
				String type = string(map, ops, "predicate_type", "");
				return switch (stripNamespace(type)) {
					case "block_match" -> BlockMatch.CODEC.decode(ops, input).map(pair -> pair.mapFirst(x -> x));
					case "tag_match" -> TagMatch.CODEC.decode(ops, input).map(pair -> pair.mapFirst(x -> x));
					case "height_match" -> HeightMatch.CODEC.decode(ops, input).map(pair -> pair.mapFirst(x -> x));
					case "all_of" -> AllOf.CODEC.decode(ops, input).map(pair -> pair.mapFirst(x -> x));
					default -> DataResult.error(() -> "Unsupported rule test: " + type);
				};
			});
		}

		@Override
		public <T> DataResult<T> encode(RuleTest input, DynamicOps<T> ops, T prefix) {
			if (input instanceof BlockMatch blockMatch) return BlockMatch.CODEC.encode(blockMatch, ops, prefix);
			if (input instanceof TagMatch tagMatch) return TagMatch.CODEC.encode(tagMatch, ops, prefix);
			if (input instanceof HeightMatch heightMatch) return HeightMatch.CODEC.encode(heightMatch, ops, prefix);
			if (input instanceof AllOf allOf) return AllOf.CODEC.encode(allOf, ops, prefix);
			return DataResult.error(() -> "Unsupported rule test: " + input.getClass().getSimpleName());
		}
	};

	static RuleTest block(Identifier block) {
		return new BlockMatch(block);
	}

	static RuleTest tag(Identifier tag) {
		return new TagMatch(stripTagPrefix(tag.toString()));
	}

	static RuleTest height(int minInclusive, int maxInclusive) {
		return new HeightMatch(minInclusive, maxInclusive);
	}

	static RuleTest allOf(RuleTest... rules) {
		return new AllOf(List.of(rules));
	}

	record BlockMatch(Identifier block) implements RuleTest {
		static final Codec<BlockMatch> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("predicate_type").forGetter(x -> "minecraft:block_match"),
				Identifier.CODEC.fieldOf("block").forGetter(BlockMatch::block)
		).apply(i, (type, block) -> new BlockMatch(block)));
	}

	record TagMatch(String tag) implements RuleTest {
		static final Codec<TagMatch> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("predicate_type").forGetter(x -> "minecraft:tag_match"),
				Codec.STRING.fieldOf("tag").forGetter(TagMatch::tag)
		).apply(i, (type, tag) -> new TagMatch(tag)));
	}

	record HeightMatch(int minInclusive, int maxInclusive) implements RuleTest {
		static final Codec<HeightMatch> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("predicate_type").forGetter(x -> "minecraft:height_match"),
				Codec.INT.fieldOf("min_inclusive").forGetter(HeightMatch::minInclusive),
				Codec.INT.fieldOf("max_inclusive").forGetter(HeightMatch::maxInclusive)
		).apply(i, (type, minInclusive, maxInclusive) -> new HeightMatch(minInclusive, maxInclusive)));
	}

	record AllOf(List<RuleTest> rules) implements RuleTest {
		static final Codec<AllOf> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("predicate_type").forGetter(x -> "minecraft:all_of"),
				RuleTest.CODEC.listOf().fieldOf("rules").forGetter(AllOf::rules)
		).apply(i, (type, rules) -> new AllOf(rules)));
	}

	private static <T> String string(MapLike<T> map, DynamicOps<T> ops, String key, String fallback) {
		T value = map.get(key);
		return value == null ? fallback : ops.getStringValue(value).result().orElse(fallback);
	}

	private static String stripNamespace(String value) {
		int separator = value.indexOf(':');
		return separator >= 0 ? value.substring(separator + 1) : value;
	}

	private static String stripTagPrefix(String tag) {
		return tag != null && tag.startsWith("#") ? tag.substring(1) : tag;
	}
}
