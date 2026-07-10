package net.vampirestudios.arrp.data.worldgen.feature;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.arrp.data.worldgen.HeightProvider;
import net.vampirestudios.arrp.data.worldgen.IntProvider;
import net.vampirestudios.arrp.data.worldgen.VerticalAnchor;
import net.vampirestudios.arrp.data.worldgen.WorldgenBlockState;

import java.util.ArrayList;
import java.util.List;

public class PlacedFeature {
	public static final Codec<PlacedFeature> CODEC = RecordCodecBuilder.create(i -> i.group(
			FeatureReference.CODEC.fieldOf("feature").forGetter(x -> x.feature),
			PlacementModifier.CODEC.listOf().fieldOf("placement").forGetter(x -> x.placement)
	).apply(i, (feature, placement) -> new PlacedFeature().feature(feature).placement(placement)));

	private FeatureReference feature;
	private List<PlacementModifier> placement = new ArrayList<>();

	public static PlacedFeature placed() {
		return new PlacedFeature();
	}

	public static PlacedFeature placed(Identifier featureId) {
		return placedReference(featureId);
	}

	public static PlacedFeature placed(Feature feature) {
		return placedInline(feature);
	}

	public static PlacedFeature placedReference(Identifier featureId) {
		return new PlacedFeature().featureId(featureId);
	}

	public static PlacedFeature placedInline(Feature feature) {
		return new PlacedFeature().feature(feature);
	}

	public PlacedFeature featureId(Identifier id) {
		return feature(FeatureReference.id(id));
	}

	public PlacedFeature feature(Feature feature) {
		return feature(FeatureReference.inline(feature));
	}

	public PlacedFeature feature(FeatureReference feature) {
		this.feature = feature;
		return this;
	}

	public PlacedFeature placement(List<PlacementModifier> placement) {
		this.placement = new ArrayList<>(placement);
		return this;
	}

	public PlacedFeature modifier(PlacementModifier modifier) {
		if (modifier != null) this.placement.add(modifier);
		return this;
	}

	public PlacedFeature count(int count) {
		return count(IntProvider.constant(count));
	}

	public PlacedFeature count(IntProvider count) {
		return modifier(new CountPlacement(count));
	}

	public PlacedFeature countOnEveryLayer(int count) {
		return modifier(new CountOnEveryLayerPlacement(count));
	}

	public PlacedFeature rarityFilter(int chance) {
		return modifier(new RarityFilterPlacement(chance));
	}

	public PlacedFeature inSquare() {
		return modifier(new InSquarePlacement());
	}

	public PlacedFeature heightmap(String heightmap) {
		return modifier(new HeightmapPlacement(heightmap));
	}

	public PlacedFeature heightRange(HeightProvider height) {
		return modifier(new HeightRangePlacement(height));
	}

	public PlacedFeature uniformHeight(VerticalAnchor minInclusive, VerticalAnchor maxInclusive) {
		return heightRange(HeightProvider.uniform(minInclusive, maxInclusive));
	}

	public PlacedFeature offset(int x, int y, int z) {
		return offset(IntProvider.constant(x), IntProvider.constant(y), IntProvider.constant(z));
	}

	public PlacedFeature offset(IntProvider x, IntProvider y, IntProvider z) {
		return modifier(new OffsetPlacement(x, y, z));
	}

	public PlacedFeature surfaceWaterDepthFilter(int maxWaterDepth) {
		return modifier(new SurfaceWaterDepthFilterPlacement(maxWaterDepth));
	}

	public PlacedFeature blockPredicateFilter(BlockPredicate predicate) {
		return modifier(new BlockPredicateFilterPlacement(predicate));
	}

	public PlacedFeature environmentScan(String direction, BlockPredicate targetCondition, int maxSteps) {
		return modifier(new EnvironmentScanPlacement(direction, targetCondition, null, maxSteps));
	}

	public PlacedFeature biomeFilter() {
		return modifier(new BiomeFilterPlacement());
	}

	public Feature getFeature() {
		return feature == null ? null : feature.inline();
	}

	public Identifier getFeatureId() {
		return feature == null ? null : feature.id();
	}

	public FeatureReference getFeatureReference() {
		return feature;
	}

	public List<PlacementModifier> getPlacement() {
		return List.copyOf(placement);
	}

	public interface PlacementModifier {
		Codec<PlacementModifier> CODEC = new Codec<>() {
			@Override
			public <T> DataResult<Pair<PlacementModifier, T>> decode(DynamicOps<T> ops, T input) {
				return ops.getMap(input).flatMap(map -> {
					String type = string(map, ops, "type", "");
					return switch (normalizeType(type)) {
						case "count" -> CountPlacement.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
						case "count_on_every_layer" -> CountOnEveryLayerPlacement.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
						case "rarity_filter" -> RarityFilterPlacement.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
						case "in_square" -> InSquarePlacement.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
						case "heightmap" -> HeightmapPlacement.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
						case "height_range" -> HeightRangePlacement.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
						case "offset" -> OffsetPlacement.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
						case "surface_water_depth_filter" -> SurfaceWaterDepthFilterPlacement.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
						case "block_predicate_filter" -> BlockPredicateFilterPlacement.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
						case "environment_scan" -> EnvironmentScanPlacement.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
						case "biome" -> BiomeFilterPlacement.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
						default -> DataResult.error(() -> "Unsupported placement modifier type: " + type);
					};
				});
			}

			@Override
			public <T> DataResult<T> encode(PlacementModifier input, DynamicOps<T> ops, T prefix) {
				if (input instanceof CountPlacement count) return CountPlacement.CODEC.codec().encode(count, ops, prefix);
				if (input instanceof CountOnEveryLayerPlacement countOnEveryLayer) return CountOnEveryLayerPlacement.CODEC.codec().encode(countOnEveryLayer, ops, prefix);
				if (input instanceof RarityFilterPlacement rarityFilter) return RarityFilterPlacement.CODEC.codec().encode(rarityFilter, ops, prefix);
				if (input instanceof InSquarePlacement inSquare) return InSquarePlacement.CODEC.codec().encode(inSquare, ops, prefix);
				if (input instanceof HeightmapPlacement heightmap) return HeightmapPlacement.CODEC.codec().encode(heightmap, ops, prefix);
				if (input instanceof HeightRangePlacement heightRange) return HeightRangePlacement.CODEC.codec().encode(heightRange, ops, prefix);
				if (input instanceof OffsetPlacement offset) return OffsetPlacement.CODEC.codec().encode(offset, ops, prefix);
				if (input instanceof SurfaceWaterDepthFilterPlacement waterDepth) return SurfaceWaterDepthFilterPlacement.CODEC.codec().encode(waterDepth, ops, prefix);
				if (input instanceof BlockPredicateFilterPlacement predicateFilter) return BlockPredicateFilterPlacement.CODEC.codec().encode(predicateFilter, ops, prefix);
				if (input instanceof EnvironmentScanPlacement environmentScan) return EnvironmentScanPlacement.CODEC.codec().encode(environmentScan, ops, prefix);
				if (input instanceof BiomeFilterPlacement biome) return BiomeFilterPlacement.CODEC.codec().encode(biome, ops, prefix);
				return DataResult.error(() -> "Unsupported placement modifier: " + input.getClass().getSimpleName());
			}
		};
	}

	public record CountPlacement(IntProvider count) implements PlacementModifier {
		public static final MapCodec<CountPlacement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:count"),
				IntProvider.CODEC.fieldOf("count").forGetter(CountPlacement::count)
		).apply(i, (type, count) -> new CountPlacement(count)));
	}

	public record CountOnEveryLayerPlacement(int count) implements PlacementModifier {
		public static final MapCodec<CountOnEveryLayerPlacement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:count_on_every_layer"),
				Codec.INT.fieldOf("count").forGetter(CountOnEveryLayerPlacement::count)
		).apply(i, (type, count) -> new CountOnEveryLayerPlacement(count)));
	}

	public record RarityFilterPlacement(int chance) implements PlacementModifier {
		public static final MapCodec<RarityFilterPlacement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:rarity_filter"),
				Codec.INT.fieldOf("chance").forGetter(RarityFilterPlacement::chance)
		).apply(i, (type, chance) -> new RarityFilterPlacement(chance)));
	}

	public record InSquarePlacement() implements PlacementModifier {
		public static final MapCodec<InSquarePlacement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:in_square")
		).apply(i, type -> new InSquarePlacement()));
	}

	public record HeightmapPlacement(String heightmap) implements PlacementModifier {
		public static final MapCodec<HeightmapPlacement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:heightmap"),
				Codec.STRING.fieldOf("heightmap").forGetter(HeightmapPlacement::heightmap)
		).apply(i, (type, heightmap) -> new HeightmapPlacement(heightmap)));
	}

	public record HeightRangePlacement(HeightProvider height) implements PlacementModifier {
		public static final MapCodec<HeightRangePlacement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:height_range"),
				HeightProvider.CODEC.fieldOf("height").forGetter(HeightRangePlacement::height)
		).apply(i, (type, height) -> new HeightRangePlacement(height)));
	}

	public record OffsetPlacement(IntProvider x, IntProvider y, IntProvider z) implements PlacementModifier {
		public static final MapCodec<OffsetPlacement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(v -> "minecraft:offset"),
				IntProvider.CODEC.fieldOf("x").forGetter(OffsetPlacement::x),
				IntProvider.CODEC.fieldOf("y").forGetter(OffsetPlacement::y),
				IntProvider.CODEC.fieldOf("z").forGetter(OffsetPlacement::z)
		).apply(i, (type, x, y, z) -> new OffsetPlacement(x, y, z)));
	}

	public record SurfaceWaterDepthFilterPlacement(int maxWaterDepth) implements PlacementModifier {
		public static final MapCodec<SurfaceWaterDepthFilterPlacement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:surface_water_depth_filter"),
				Codec.INT.fieldOf("max_water_depth").forGetter(SurfaceWaterDepthFilterPlacement::maxWaterDepth)
		).apply(i, (type, maxWaterDepth) -> new SurfaceWaterDepthFilterPlacement(maxWaterDepth)));
	}

	public record BlockPredicateFilterPlacement(BlockPredicate predicate) implements PlacementModifier {
		public static final MapCodec<BlockPredicateFilterPlacement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:block_predicate_filter"),
				BlockPredicate.CODEC.fieldOf("predicate").forGetter(BlockPredicateFilterPlacement::predicate)
		).apply(i, (type, predicate) -> new BlockPredicateFilterPlacement(predicate)));
	}

	public record EnvironmentScanPlacement(String direction, BlockPredicate targetCondition, BlockPredicate allowedSearchCondition, int maxSteps) implements PlacementModifier {
		public static final MapCodec<EnvironmentScanPlacement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:environment_scan"),
				Codec.STRING.fieldOf("direction_of_search").forGetter(EnvironmentScanPlacement::direction),
				BlockPredicate.CODEC.fieldOf("target_condition").forGetter(EnvironmentScanPlacement::targetCondition),
				BlockPredicate.CODEC.optionalFieldOf("allowed_search_condition").forGetter(x -> java.util.Optional.ofNullable(x.allowedSearchCondition)),
				Codec.INT.fieldOf("max_steps").forGetter(EnvironmentScanPlacement::maxSteps)
		).apply(i, (type, direction, targetCondition, allowedSearchCondition, maxSteps) ->
				new EnvironmentScanPlacement(direction, targetCondition, allowedSearchCondition.orElse(null), maxSteps)));
	}

	public record BiomeFilterPlacement() implements PlacementModifier {
		public static final MapCodec<BiomeFilterPlacement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:biome")
		).apply(i, type -> new BiomeFilterPlacement()));
	}

	public interface BlockPredicate {
		Codec<BlockPredicate> CODEC = new Codec<>() {
			@Override
			public <T> DataResult<Pair<BlockPredicate, T>> decode(DynamicOps<T> ops, T input) {
				return ops.getMap(input).flatMap(map -> {
					String type = string(map, ops, "type", "");
					return switch (normalizeType(type)) {
						case "matching_blocks" -> MatchingBlocksPredicate.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
						case "matching_block_tag" -> MatchingBlockTagPredicate.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
						case "would_survive" -> WouldSurvivePredicate.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
						case "replaceable" -> ReplaceablePredicate.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
						case "height_range" -> HeightRangePredicate.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
						case "not" -> NotPredicate.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
						case "all_of" -> AllOfPredicate.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
						case "any_of" -> AnyOfPredicate.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
						default -> DataResult.error(() -> "Unsupported block predicate type: " + type);
					};
				});
			}

			@Override
			public <T> DataResult<T> encode(BlockPredicate input, DynamicOps<T> ops, T prefix) {
				if (input instanceof MatchingBlocksPredicate matchingBlocks) return MatchingBlocksPredicate.CODEC.codec().encode(matchingBlocks, ops, prefix);
				if (input instanceof MatchingBlockTagPredicate matchingTag) return MatchingBlockTagPredicate.CODEC.codec().encode(matchingTag, ops, prefix);
				if (input instanceof WouldSurvivePredicate wouldSurvive) return WouldSurvivePredicate.CODEC.codec().encode(wouldSurvive, ops, prefix);
				if (input instanceof ReplaceablePredicate replaceable) return ReplaceablePredicate.CODEC.codec().encode(replaceable, ops, prefix);
				if (input instanceof HeightRangePredicate heightRange) return HeightRangePredicate.CODEC.codec().encode(heightRange, ops, prefix);
				if (input instanceof NotPredicate not) return NotPredicate.CODEC.codec().encode(not, ops, prefix);
				if (input instanceof AllOfPredicate allOf) return AllOfPredicate.CODEC.codec().encode(allOf, ops, prefix);
				if (input instanceof AnyOfPredicate anyOf) return AnyOfPredicate.CODEC.codec().encode(anyOf, ops, prefix);
				return DataResult.error(() -> "Unsupported block predicate: " + input.getClass().getSimpleName());
			}
		};

		static BlockPredicate matchingBlocks(Identifier... blocks) { return new MatchingBlocksPredicate(List.of(blocks), Offset.ZERO); }
		static BlockPredicate matchingBlockTag(String tag) { return new MatchingBlockTagPredicate(stripTagPrefix(tag), Offset.ZERO); }
		static BlockPredicate wouldSurvive(String block) { return new WouldSurvivePredicate(WorldgenBlockState.blockState(Identifier.tryParse(block)), Offset.ZERO); }
		static BlockPredicate wouldSurvive(WorldgenBlockState state) { return new WouldSurvivePredicate(state, Offset.ZERO); }
		static BlockPredicate replaceable() { return new ReplaceablePredicate(Offset.ZERO); }
		static BlockPredicate heightRange(VerticalAnchor minInclusive, VerticalAnchor maxInclusive) { return new HeightRangePredicate(minInclusive, maxInclusive); }
		static BlockPredicate not(BlockPredicate predicate) { return new NotPredicate(predicate); }
		static BlockPredicate allOf(BlockPredicate... predicates) { return new AllOfPredicate(List.of(predicates)); }
		static BlockPredicate anyOf(BlockPredicate... predicates) { return new AnyOfPredicate(List.of(predicates)); }
	}

	public record Offset(int x, int y, int z) {
		public static final Offset ZERO = new Offset(0, 0, 0);
		public static final Codec<Offset> CODEC = Codec.INT.listOf().xmap(list -> {
			if (list.size() != 3) return ZERO;
			return new Offset(list.get(0), list.get(1), list.get(2));
		}, offset -> List.of(offset.x, offset.y, offset.z));
	}

	public record MatchingBlocksPredicate(List<Identifier> blocks, Offset offset) implements BlockPredicate {
		public static final MapCodec<MatchingBlocksPredicate> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:matching_blocks"),
				Identifier.CODEC.listOf().fieldOf("blocks").forGetter(MatchingBlocksPredicate::blocks),
				Offset.CODEC.optionalFieldOf("offset", Offset.ZERO).forGetter(MatchingBlocksPredicate::offset)
		).apply(i, (type, blocks, offset) -> new MatchingBlocksPredicate(blocks, offset)));
	}

	public record MatchingBlockTagPredicate(String tag, Offset offset) implements BlockPredicate {
		public static final MapCodec<MatchingBlockTagPredicate> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:matching_block_tag"),
				Codec.STRING.fieldOf("tag").forGetter(MatchingBlockTagPredicate::tag),
				Offset.CODEC.optionalFieldOf("offset", Offset.ZERO).forGetter(MatchingBlockTagPredicate::offset)
		).apply(i, (type, tag, offset) -> new MatchingBlockTagPredicate(tag, offset)));
	}

	public record WouldSurvivePredicate(WorldgenBlockState state, Offset offset) implements BlockPredicate {
		public static final MapCodec<WouldSurvivePredicate> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:would_survive"),
				WorldgenBlockState.CODEC.fieldOf("state").forGetter(WouldSurvivePredicate::state),
				Offset.CODEC.optionalFieldOf("offset", Offset.ZERO).forGetter(WouldSurvivePredicate::offset)
		).apply(i, (type, state, offset) -> new WouldSurvivePredicate(state, offset)));
	}

	public record ReplaceablePredicate(Offset offset) implements BlockPredicate {
		public static final MapCodec<ReplaceablePredicate> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:replaceable"),
				Offset.CODEC.optionalFieldOf("offset", Offset.ZERO).forGetter(ReplaceablePredicate::offset)
		).apply(i, (type, offset) -> new ReplaceablePredicate(offset)));
	}

	public record HeightRangePredicate(VerticalAnchor minInclusive, VerticalAnchor maxInclusive) implements BlockPredicate {
		public static final MapCodec<HeightRangePredicate> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:height_range"),
				VerticalAnchor.CODEC.fieldOf("min_inclusive").forGetter(HeightRangePredicate::minInclusive),
				VerticalAnchor.CODEC.fieldOf("max_inclusive").forGetter(HeightRangePredicate::maxInclusive)
		).apply(i, (type, minInclusive, maxInclusive) -> new HeightRangePredicate(minInclusive, maxInclusive)));
	}

	public record NotPredicate(BlockPredicate predicate) implements BlockPredicate {
		public static final MapCodec<NotPredicate> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:not"),
				BlockPredicate.CODEC.fieldOf("predicate").forGetter(NotPredicate::predicate)
		).apply(i, (type, predicate) -> new NotPredicate(predicate)));
	}

	public record AllOfPredicate(List<BlockPredicate> predicates) implements BlockPredicate {
		public static final MapCodec<AllOfPredicate> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:all_of"),
				BlockPredicate.CODEC.listOf().fieldOf("predicates").forGetter(AllOfPredicate::predicates)
		).apply(i, (type, predicates) -> new AllOfPredicate(predicates)));
	}

	public record AnyOfPredicate(List<BlockPredicate> predicates) implements BlockPredicate {
		public static final MapCodec<AnyOfPredicate> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:any_of"),
				BlockPredicate.CODEC.listOf().fieldOf("predicates").forGetter(AnyOfPredicate::predicates)
		).apply(i, (type, predicates) -> new AnyOfPredicate(predicates)));
	}

	private static String normalizeType(String type) {
		int separator = type.indexOf(':');
		return separator >= 0 ? type.substring(separator + 1) : type;
	}

	private static <T> String string(MapLike<T> map, DynamicOps<T> ops, String key, String fallback) {
		T value = map.get(key);
		return value == null ? fallback : ops.getStringValue(value).result().orElse(fallback);
	}

	private static String stripTagPrefix(String tag) {
		return tag != null && tag.startsWith("#") ? tag.substring(1) : tag;
	}
}
