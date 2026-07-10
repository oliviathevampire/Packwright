package net.vampirestudios.packwright.data.worldgen.feature;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.FloatProvider;
import net.vampirestudios.packwright.data.worldgen.HeightProvider;
import net.vampirestudios.packwright.data.worldgen.IntProvider;
import net.vampirestudios.packwright.data.worldgen.material.MaterialCondition;
import net.vampirestudios.packwright.data.worldgen.material.MaterialRule;
import net.vampirestudios.packwright.data.worldgen.VerticalAnchor;
import net.vampirestudios.packwright.data.worldgen.WorldgenBlockState;
import net.vampirestudios.packwright.data.worldgen.feature.foliage.TreeFoliagePlacer;
import net.vampirestudios.packwright.data.worldgen.feature.tree.decorator.TreeDecorator;
import net.vampirestudios.packwright.data.worldgen.feature.tree.root.TreeRootPlacer;
import net.vampirestudios.packwright.data.worldgen.feature.tree.size.TreeFeatureSize;
import net.vampirestudios.packwright.data.worldgen.feature.trunk.TreeTrunkPlacer;

import java.util.List;

public class FeatureProperties {
	private final JsonObject values;

	public FeatureProperties() {
		this(new JsonObject());
	}

	public FeatureProperties(JsonObject values) {
		this.values = values == null ? new JsonObject() : values.deepCopy();
	}

	public static FeatureProperties properties() {
		return new FeatureProperties();
	}

	public FeatureProperties put(String key, JsonElement value) {
		values.add(key, value);
		return this;
	}

	public FeatureProperties put(String key, String value) {
		values.addProperty(key, value);
		return this;
	}

	public FeatureProperties put(String key, Identifier value) {
		return put(key, Identifier.CODEC, value);
	}

	public FeatureProperties put(String key, int value) {
		values.addProperty(key, value);
		return this;
	}

	public FeatureProperties put(String key, float value) {
		values.addProperty(key, value);
		return this;
	}

	public FeatureProperties put(String key, boolean value) {
		values.addProperty(key, value);
		return this;
	}

	public FeatureProperties put(String key, Object value) {
		values.add(key, json(value));
		return this;
	}

	public FeatureProperties put(String key, IntProvider value) {
		return put(key, IntProvider.CODEC, value);
	}

	public FeatureProperties put(String key, WorldgenBlockState value) {
		return put(key, WorldgenBlockState.CODEC, value);
	}

	public <T> FeatureProperties put(String key, Codec<T> codec, T value) {
		values.add(key, codec.encodeStart(JsonOps.INSTANCE, value).getOrThrow());
		return this;
	}

	public <T> FeatureProperties putList(String key, List<T> list, Codec<T> codec) {
		JsonArray array = new JsonArray();
		for (T value : list) {
			array.add(codec.encodeStart(JsonOps.INSTANCE, value).getOrThrow());
		}
		values.add(key, array);
		return this;
	}

	public FeatureProperties putElements(String key, List<? extends JsonElement> list) {
		JsonArray array = new JsonArray();
		for (JsonElement value : list) {
			array.add(value);
		}
		values.add(key, array);
		return this;
	}

	public JsonObject asJsonObject() {
		return values.deepCopy();
	}

	public static JsonElement json(Object value) {
		switch (value) {
			case null -> {
				return JsonNull.INSTANCE;
			}
			case JsonElement element -> {
				return element;
			}
			case String string -> {
				return new JsonPrimitive(string);
			}
			case Identifier id -> {
				return new JsonPrimitive(id.toString());
			}
			case Number number -> {
				return new JsonPrimitive(number);
			}
			case Boolean bool -> {
				return new JsonPrimitive(bool);
			}
			case WorldgenBlockState state -> {
				return encode(WorldgenBlockState.CODEC, state);
			}
			case IntProvider intProvider -> {
				return encode(IntProvider.CODEC, intProvider);
			}
			case FloatProvider floatProvider -> {
				return encode(FloatProvider.CODEC, floatProvider);
			}
			case HeightProvider heightProvider -> {
				return encode(HeightProvider.CODEC, heightProvider);
			}
			case VerticalAnchor anchor -> {
				return encode(VerticalAnchor.CODEC, anchor);
			}
			case BlockStateProvider provider -> {
				return encode(BlockStateProvider.CODEC, provider);
			}
			case RuleTest ruleTest -> {
				return encode(RuleTest.CODEC, ruleTest);
			}
			case OreTarget oreTarget -> {
				return encode(OreTarget.CODEC, oreTarget);
			}
			case WeightedPlacedFeature weightedPlacedFeature -> {
				return encode(WeightedPlacedFeature.CODEC, weightedPlacedFeature);
			}
			case PlacedFeature placedFeature -> {
				return encode(PlacedFeature.CODEC, placedFeature);
			}
			case TreeTrunkPlacer trunkPlacer -> {
				return encode(TreeTrunkPlacer.CODEC, trunkPlacer);
			}
			case TreeFoliagePlacer foliagePlacer -> {
				return encode(TreeFoliagePlacer.CODEC, foliagePlacer);
			}
			case TreeDecorator decorator -> {
				return encode(TreeDecorator.CODEC, decorator);
			}
			case TreeFeatureSize featureSize -> {
				return encode(TreeFeatureSize.CODEC, featureSize);
			}
			case TreeRootPlacer rootPlacer -> {
				return encode(TreeRootPlacer.CODEC, rootPlacer);
			}
			case MaterialRule materialRule -> {
				return encode(MaterialRule.CODEC, materialRule);
			}
			case MaterialCondition materialCondition -> {
				return encode(MaterialCondition.CODEC, materialCondition);
			}
			case List<?> list -> {
				JsonArray array = new JsonArray();
				for (Object entry : list) array.add(json(entry));
				return array;
			}
			default -> {
			}
		}
		throw new IllegalArgumentException("Unsupported feature property value: " + value.getClass().getName());
	}

	public static JsonObject object(Object... keyValues) {
		if (keyValues.length % 2 != 0) {
			throw new IllegalArgumentException("FeatureProperties.object requires key/value pairs");
		}

		JsonObject object = new JsonObject();
		for (int i = 0; i < keyValues.length; i += 2) {
			if (!(keyValues[i] instanceof String key)) {
				throw new IllegalArgumentException("FeatureProperties.object keys must be strings");
			}
			object.add(key, json(keyValues[i + 1]));
		}
		return object;
	}

	public static <T> JsonElement encode(Codec<T> codec, T value) {
		return codec.encodeStart(JsonOps.INSTANCE, value).getOrThrow();
	}

}
