package net.vampirestudios.packwright.data.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.JavaOps;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.FloatProvider;
import net.vampirestudios.packwright.data.worldgen.HeightProvider;
import net.vampirestudios.packwright.data.worldgen.IntProvider;
import net.vampirestudios.packwright.data.worldgen.VerticalAnchor;
import net.vampirestudios.packwright.data.worldgen.WorldgenBlockState;
import net.vampirestudios.packwright.data.worldgen.feature.foliage.TreeFoliagePlacer;
import net.vampirestudios.packwright.data.worldgen.feature.tree.decorator.TreeDecorator;
import net.vampirestudios.packwright.data.worldgen.feature.tree.root.TreeRootPlacer;
import net.vampirestudios.packwright.data.worldgen.feature.tree.size.TreeFeatureSize;
import net.vampirestudios.packwright.data.worldgen.feature.trunk.TreeTrunkPlacer;
import net.vampirestudios.packwright.data.worldgen.material.MaterialCondition;
import net.vampirestudios.packwright.data.worldgen.material.MaterialRule;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** the type-specific fields of a {@link Feature}, keyed by name */
public record FeatureProperties(Map<String, Object> values) {
	public FeatureProperties() {
		this(new LinkedHashMap<>());
	}

	public FeatureProperties(Map<String, Object> values) {
		this.values = values == null ? new LinkedHashMap<>() : new LinkedHashMap<>(values);
	}

	public static FeatureProperties properties() {
		return new FeatureProperties();
	}

	public FeatureProperties put(String key, String value) {
		values.put(key, value);
		return this;
	}

	public FeatureProperties put(String key, Identifier value) {
		values.put(key, value == null ? null : value.toString());
		return this;
	}

	public FeatureProperties put(String key, int value) {
		values.put(key, value);
		return this;
	}

	public FeatureProperties put(String key, float value) {
		values.put(key, value);
		return this;
	}

	public FeatureProperties put(String key, boolean value) {
		values.put(key, value);
		return this;
	}

	public FeatureProperties put(String key, Object value) {
		values.put(key, encodeValue(value));
		return this;
	}

	public FeatureProperties put(String key, IntProvider value) {
		return put(key, IntProvider.CODEC, value);
	}

	public FeatureProperties put(String key, WorldgenBlockState value) {
		return put(key, WorldgenBlockState.CODEC, value);
	}

	public <T> FeatureProperties put(String key, Codec<T> codec, T value) {
		values.put(key, encode(codec, value));
		return this;
	}

	public <T> FeatureProperties putList(String key, List<T> list, Codec<T> codec) {
		values.put(key, list.stream().map(value -> encode(codec, value)).toList());
		return this;
	}

	/** the type-specific fields as plain Java values */
	@Override
	public Map<String, Object> values() {
		return new LinkedHashMap<>(values);
	}

	private static Object encodeValue(Object value) {
		return switch (value) {
			case null -> null;
			case String string -> string;
			case Identifier id -> id.toString();
			case Number number -> number;
			case Boolean bool -> bool;
			case WorldgenBlockState state -> encode(WorldgenBlockState.CODEC, state);
			case IntProvider intProvider -> encode(IntProvider.CODEC, intProvider);
			case FloatProvider floatProvider -> encode(FloatProvider.CODEC, floatProvider);
			case HeightProvider heightProvider -> encode(HeightProvider.CODEC, heightProvider);
			case VerticalAnchor anchor -> encode(VerticalAnchor.CODEC, anchor);
			case BlockStateProvider provider -> encode(BlockStateProvider.CODEC, provider);
			case RuleTest ruleTest -> encode(RuleTest.CODEC, ruleTest);
			case OreTarget oreTarget -> encode(OreTarget.CODEC, oreTarget);
			case WeightedPlacedFeature weightedPlacedFeature -> encode(WeightedPlacedFeature.CODEC, weightedPlacedFeature);
			case PlacedFeature placedFeature -> encode(PlacedFeature.CODEC, placedFeature);
			case TreeTrunkPlacer trunkPlacer -> encode(TreeTrunkPlacer.CODEC, trunkPlacer);
			case TreeFoliagePlacer foliagePlacer -> encode(TreeFoliagePlacer.CODEC, foliagePlacer);
			case TreeDecorator decorator -> encode(TreeDecorator.CODEC, decorator);
			case TreeFeatureSize featureSize -> encode(TreeFeatureSize.CODEC, featureSize);
			case TreeRootPlacer rootPlacer -> encode(TreeRootPlacer.CODEC, rootPlacer);
			case MaterialRule materialRule -> encode(MaterialRule.CODEC, materialRule);
			case MaterialCondition materialCondition -> encode(MaterialCondition.CODEC, materialCondition);
			case List<?> list -> list.stream().map(FeatureProperties::encodeValue).toList();
			default -> throw new IllegalArgumentException("Unsupported feature property value: " + value.getClass().getName());
		};
	}

	public static <T> Object encode(Codec<T> codec, T value) {
		return codec.encodeStart(JavaOps.INSTANCE, value).getOrThrow();
	}
}
