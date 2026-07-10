package net.vampirestudios.packwright.data.worldgen.feature;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.WorldgenBlockState;

import java.util.List;

public sealed interface BlockStateProvider permits BlockStateProvider.Simple, BlockStateProvider.CopyProperties, BlockStateProvider.Weighted, BlockStateProvider.RuleBased, BlockStateProvider.RandomizedInt {
	Codec<BlockStateProvider> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<BlockStateProvider, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> switch (normalizeType(string(map, ops, "type", ""))) {
				case "simple_state_provider" -> BlockStateProvider.Simple.CODEC.decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "copy_properties_provider" -> CopyProperties.CODEC.decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "weighted_state_provider" -> Weighted.CODEC.decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "rule_based_state_provider" -> RuleBased.CODEC.decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "randomized_int_state_provider" -> RandomizedInt.CODEC.decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				default -> DataResult.error(() -> "Unsupported block state provider type");
			});
		}

		@Override
		public <T> DataResult<T> encode(BlockStateProvider input, DynamicOps<T> ops, T prefix) {
			if (input instanceof BlockStateProvider.Simple simple) return BlockStateProvider.Simple.CODEC.encode(simple, ops, prefix);
			if (input instanceof CopyProperties copyProperties) return CopyProperties.CODEC.encode(copyProperties, ops, prefix);
			if (input instanceof Weighted weighted) return Weighted.CODEC.encode(weighted, ops, prefix);
			if (input instanceof RuleBased ruleBased) return RuleBased.CODEC.encode(ruleBased, ops, prefix);
			if (input instanceof RandomizedInt randomizedInt) return RandomizedInt.CODEC.encode(randomizedInt, ops, prefix);
			return DataResult.error(() -> "Unsupported block state provider: " + input.getClass().getSimpleName());
		}
	};

	static BlockStateProvider simple(Identifier block) {
		return new Simple(WorldgenBlockState.blockState(block));
	}

	static BlockStateProvider simple(WorldgenBlockState state) {
		return new Simple(state);
	}

	static BlockStateProvider copyProperties(BlockStateProvider provider) {
		return new CopyProperties(provider);
	}

	static BlockStateProvider weighted(WeightedEntry... entries) {
		return new Weighted(List.of(entries));
	}

	static WeightedEntry weightedEntry(Identifier block, int weight) {
		return new WeightedEntry(WorldgenBlockState.blockState(block), weight);
	}

	static WeightedEntry weightedEntry(WorldgenBlockState state, int weight) {
		return new WeightedEntry(state, weight);
	}

	static BlockStateProvider ruleBased(BlockStateProvider fallback, Rule rule) {
		return new RuleBased(fallback, List.of(rule));
	}

	static Rule rule(PlacedFeature.BlockPredicate ifTrue, BlockStateProvider then) {
		return new Rule(ifTrue, then);
	}

	static BlockStateProvider randomizedInt(BlockStateProvider source, String property, net.vampirestudios.packwright.data.worldgen.IntProvider values) {
		return new RandomizedInt(source, property, values);
	}

	record Simple(WorldgenBlockState state) implements BlockStateProvider {
		static final Codec<Simple> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:simple_state_provider"),
				WorldgenBlockState.CODEC.fieldOf("state").forGetter(Simple::state)
		).apply(i, (type, state) -> new Simple(state)));
	}

	record CopyProperties(BlockStateProvider blockStateProvider) implements BlockStateProvider {
		static final Codec<CopyProperties> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:copy_properties_provider"),
				BlockStateProvider.CODEC.fieldOf("block_state_provider").forGetter(CopyProperties::blockStateProvider)
		).apply(i, (type, blockStateProvider) -> new CopyProperties(blockStateProvider)));
	}

	record Weighted(List<WeightedEntry> entries) implements BlockStateProvider {
		static final Codec<Weighted> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:weighted_state_provider"),
				WeightedEntry.CODEC.listOf().fieldOf("entries").forGetter(Weighted::entries)
		).apply(i, (type, entries) -> new Weighted(entries)));
	}

	record WeightedEntry(WorldgenBlockState data, int weight) {
		static final Codec<WeightedEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
				WorldgenBlockState.CODEC.fieldOf("data").forGetter(WeightedEntry::data),
				Codec.INT.fieldOf("weight").forGetter(WeightedEntry::weight)
		).apply(i, WeightedEntry::new));
	}

	record RuleBased(BlockStateProvider fallback, List<Rule> rules) implements BlockStateProvider {
		static final Codec<RuleBased> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:rule_based_state_provider"),
				BlockStateProvider.CODEC.fieldOf("fallback").forGetter(RuleBased::fallback),
				Rule.CODEC.listOf().fieldOf("rules").forGetter(RuleBased::rules)
		).apply(i, (type, fallback, rules) -> new RuleBased(fallback, rules)));
	}

	record Rule(PlacedFeature.BlockPredicate ifTrue, BlockStateProvider then) {
		static final Codec<Rule> CODEC = RecordCodecBuilder.create(i -> i.group(
				PlacedFeature.BlockPredicate.CODEC.fieldOf("if_true").forGetter(Rule::ifTrue),
				BlockStateProvider.CODEC.fieldOf("then").forGetter(Rule::then)
		).apply(i, Rule::new));
	}

	record RandomizedInt(BlockStateProvider source, String property, net.vampirestudios.packwright.data.worldgen.IntProvider values) implements BlockStateProvider {
		static final Codec<RandomizedInt> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:randomized_int_state_provider"),
				BlockStateProvider.CODEC.fieldOf("source").forGetter(RandomizedInt::source),
				Codec.STRING.fieldOf("property").forGetter(RandomizedInt::property),
				net.vampirestudios.packwright.data.worldgen.IntProvider.CODEC.fieldOf("values").forGetter(RandomizedInt::values)
		).apply(i, (type, source, property, values) -> new RandomizedInt(source, property, values)));
	}

	private static String normalizeType(String type) {
		int separator = type.indexOf(':');
		return separator >= 0 ? type.substring(separator + 1) : type;
	}

	private static <T> String string(MapLike<T> map, DynamicOps<T> ops, String key, String fallback) {
		T value = map.get(key);
		return value == null ? fallback : ops.getStringValue(value).result().orElse(fallback);
	}
}
