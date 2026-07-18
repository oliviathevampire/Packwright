package net.vampirestudios.packwright.data.worldgen.feature;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.WorldgenBlockState;
import net.vampirestudios.packwright.data.worldgen.noise.NoiseParameters;

import java.util.List;
import java.util.Optional;

public sealed interface BlockStateProvider permits BlockStateProvider.Simple, BlockStateProvider.CopyProperties, BlockStateProvider.Weighted, BlockStateProvider.RuleBased, BlockStateProvider.RandomizedInt, BlockStateProvider.Noise, BlockStateProvider.NoiseThreshold, BlockStateProvider.DualNoise, BlockStateProvider.RandomBlock, BlockStateProvider.RotatedBlock {
	Codec<BlockStateProvider> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<BlockStateProvider, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> switch (normalizeType(string(map, ops, "type", ""))) {
				case "simple_state_provider" -> BlockStateProvider.Simple.CODEC.decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "copy_properties_provider" -> CopyProperties.CODEC.decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "weighted_state_provider" -> Weighted.CODEC.decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "rule_based_state_provider" -> RuleBased.CODEC.decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "randomized_int_state_provider" -> RandomizedInt.CODEC.decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "noise_provider" -> Noise.CODEC.decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "noise_threshold_provider" -> NoiseThreshold.CODEC.decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "dual_noise_provider" -> DualNoise.CODEC.decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "random_block_provider" -> RandomBlock.CODEC.decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "rotated_block_provider" -> RotatedBlock.CODEC.decode(ops, input).map(pair -> pair.mapFirst(x -> x));
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
			if (input instanceof Noise noise) return Noise.CODEC.encode(noise, ops, prefix);
			if (input instanceof NoiseThreshold noiseThreshold) return NoiseThreshold.CODEC.encode(noiseThreshold, ops, prefix);
			if (input instanceof DualNoise dualNoise) return DualNoise.CODEC.encode(dualNoise, ops, prefix);
			if (input instanceof RandomBlock randomBlock) return RandomBlock.CODEC.encode(randomBlock, ops, prefix);
			if (input instanceof RotatedBlock rotatedBlock) return RotatedBlock.CODEC.encode(rotatedBlock, ops, prefix);
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
		return new RuleBased(Optional.of(fallback), List.of(rule));
	}

	static BlockStateProvider ruleBased(Rule rule) {
		return new RuleBased(Optional.empty(), List.of(rule));
	}

	static Rule rule(PlacedFeature.BlockPredicate ifTrue, BlockStateProvider then) {
		return new Rule(ifTrue, then);
	}

	static BlockStateProvider randomizedInt(BlockStateProvider source, String property, net.vampirestudios.packwright.data.worldgen.IntProvider values) {
		return new RandomizedInt(source, property, values);
	}

	static BlockStateProvider noise(long seed, NoiseParameters noise, float scale, List<WorldgenBlockState> states) {
		return new Noise(seed, noise, scale, states);
	}

	static BlockStateProvider noiseThreshold(
			long seed, NoiseParameters noise, float scale, float threshold, float highChance,
			WorldgenBlockState defaultState, List<WorldgenBlockState> lowStates, List<WorldgenBlockState> highStates
	) {
		return new NoiseThreshold(seed, noise, scale, threshold, highChance, defaultState, lowStates, highStates);
	}

	static BlockStateProvider dualNoise(
			int varietyMin, int varietyMax, NoiseParameters slowNoise, float slowScale,
			long seed, NoiseParameters noise, float scale, List<WorldgenBlockState> states
	) {
		return new DualNoise(varietyMin, varietyMax, slowNoise, slowScale, seed, noise, scale, states);
	}

	static BlockStateProvider randomBlock(List<Identifier> blocks) {
		return new RandomBlock(blocks);
	}

	static BlockStateProvider rotatedBlock(BlockStateProvider state) {
		return new RotatedBlock(state, Optional.empty());
	}

	static BlockStateProvider rotatedBlock(BlockStateProvider state, String direction) {
		return new RotatedBlock(state, Optional.of(direction));
	}

	record Simple(WorldgenBlockState state) implements BlockStateProvider {
		static final Codec<Simple> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:simple_state_provider"),
				WorldgenBlockState.CODEC.fieldOf("state").forGetter(Simple::state)
		).apply(i, (type, state) -> new Simple(state)));
	}

	record CopyProperties(BlockStateProvider source) implements BlockStateProvider {
		static final Codec<CopyProperties> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:copy_properties_provider"),
				BlockStateProvider.CODEC.fieldOf("source").forGetter(CopyProperties::source)
		).apply(i, (type, source) -> new CopyProperties(source)));
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

	record RuleBased(Optional<BlockStateProvider> fallback, List<Rule> rules) implements BlockStateProvider {
		static final Codec<RuleBased> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:rule_based_state_provider"),
				BlockStateProvider.CODEC.optionalFieldOf("fallback").forGetter(RuleBased::fallback),
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

	record Noise(long seed, NoiseParameters noise, float scale, List<WorldgenBlockState> states) implements BlockStateProvider {
		static final Codec<Noise> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:noise_provider"),
				Codec.LONG.fieldOf("seed").forGetter(Noise::seed),
				NoiseParameters.CODEC.fieldOf("noise").forGetter(Noise::noise),
				Codec.FLOAT.fieldOf("scale").forGetter(Noise::scale),
				WorldgenBlockState.CODEC.listOf().fieldOf("states").forGetter(Noise::states)
		).apply(i, (type, seed, noise, scale, states) -> new Noise(seed, noise, scale, states)));
	}

	record NoiseThreshold(
			long seed, NoiseParameters noise, float scale, float threshold, float highChance,
			WorldgenBlockState defaultState, List<WorldgenBlockState> lowStates, List<WorldgenBlockState> highStates
	) implements BlockStateProvider {
		static final Codec<NoiseThreshold> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:noise_threshold_provider"),
				Codec.LONG.fieldOf("seed").forGetter(NoiseThreshold::seed),
				NoiseParameters.CODEC.fieldOf("noise").forGetter(NoiseThreshold::noise),
				Codec.FLOAT.fieldOf("scale").forGetter(NoiseThreshold::scale),
				Codec.FLOAT.fieldOf("threshold").forGetter(NoiseThreshold::threshold),
				Codec.FLOAT.fieldOf("high_chance").forGetter(NoiseThreshold::highChance),
				WorldgenBlockState.CODEC.fieldOf("default_state").forGetter(NoiseThreshold::defaultState),
				WorldgenBlockState.CODEC.listOf().fieldOf("low_states").forGetter(NoiseThreshold::lowStates),
				WorldgenBlockState.CODEC.listOf().fieldOf("high_states").forGetter(NoiseThreshold::highStates)
		).apply(i, (type, seed, noise, scale, threshold, highChance, defaultState, lowStates, highStates) ->
				new NoiseThreshold(seed, noise, scale, threshold, highChance, defaultState, lowStates, highStates)));
	}

	record DualNoise(
			int varietyMin, int varietyMax, NoiseParameters slowNoise, float slowScale,
			long seed, NoiseParameters noise, float scale, List<WorldgenBlockState> states
	) implements BlockStateProvider {
		static final Codec<DualNoise> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:dual_noise_provider"),
				Codec.INT.fieldOf("min_inclusive").forGetter(DualNoise::varietyMin),
				Codec.INT.fieldOf("max_inclusive").forGetter(DualNoise::varietyMax),
				NoiseParameters.CODEC.fieldOf("slow_noise").forGetter(DualNoise::slowNoise),
				Codec.FLOAT.fieldOf("slow_scale").forGetter(DualNoise::slowScale),
				Codec.LONG.fieldOf("seed").forGetter(DualNoise::seed),
				NoiseParameters.CODEC.fieldOf("noise").forGetter(DualNoise::noise),
				Codec.FLOAT.fieldOf("scale").forGetter(DualNoise::scale),
				WorldgenBlockState.CODEC.listOf().fieldOf("states").forGetter(DualNoise::states)
		).apply(i, (type, varietyMin, varietyMax, slowNoise, slowScale, seed, noise, scale, states) ->
				new DualNoise(varietyMin, varietyMax, slowNoise, slowScale, seed, noise, scale, states)));
	}

	/** {@code blocks} is a block id list/tag reference, matching {@code can_grow_through}-style fields elsewhere in this project */
	record RandomBlock(List<Identifier> blocks) implements BlockStateProvider {
		static final Codec<RandomBlock> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:random_block_provider"),
				Identifier.CODEC.listOf().fieldOf("blocks").forGetter(RandomBlock::blocks)
		).apply(i, (type, blocks) -> new RandomBlock(blocks)));
	}

	record RotatedBlock(BlockStateProvider state, Optional<String> direction) implements BlockStateProvider {
		static final Codec<RotatedBlock> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:rotated_block_provider"),
				BlockStateProvider.CODEC.fieldOf("state").forGetter(RotatedBlock::state),
				Codec.STRING.optionalFieldOf("direction").forGetter(RotatedBlock::direction)
		).apply(i, (type, state, direction) -> new RotatedBlock(state, direction)));
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
