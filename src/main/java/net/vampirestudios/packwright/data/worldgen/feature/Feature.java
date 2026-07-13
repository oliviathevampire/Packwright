package net.vampirestudios.packwright.data.worldgen.feature;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.minecraft.resources.Identifier;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Feature {
	public static final Codec<Feature> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<Feature, T>> decode(DynamicOps<T> ops, T input) {
			Object value = new Dynamic<>(ops, input).convert(JavaOps.INSTANCE).getValue();
			if (!(value instanceof Map<?, ?> map)) return DataResult.error(() -> "Feature must be an object");

			Object typeValue = map.get("type");
			if (!(typeValue instanceof String type)) {
				return DataResult.error(() -> "Feature is missing type");
			}

			Map<String, Object> properties = new LinkedHashMap<>();
			for (Map.Entry<?, ?> entry : map.entrySet()) {
				String key = String.valueOf(entry.getKey());
				if (!key.equals("type")) properties.put(key, entry.getValue());
			}
			return DataResult.success(Pair.of(new Feature(type, new FeatureProperties(properties)), input));
		}

		@Override
		public <T> DataResult<T> encode(Feature feature, DynamicOps<T> ops, T prefix) {
			Map<String, Object> object = new LinkedHashMap<>();
			if (feature.type != null) object.put("type", feature.type);
			object.putAll(feature.properties.values());
			return DataResult.success(new Dynamic<>(JavaOps.INSTANCE, object).convert(ops).getValue());
		}
	};

	private String type = "minecraft:simple_block";
	private FeatureProperties properties = FeatureProperties.properties();

	public Feature() {
	}

	public Feature(String type, FeatureProperties properties) {
		this.type = type;
		this.properties = properties == null ? FeatureProperties.properties() : properties;
	}

	public static Feature feature() {
		return new Feature();
	}

	public static Feature of(String type) {
		return feature().type(type);
	}

	public static Feature of(String type, FeatureProperties properties) {
		return new Feature(type, properties);
	}

	public Feature type(String type) {
		this.type = type;
		return this;
	}

	public Feature properties(FeatureProperties properties) {
		this.properties = properties == null ? FeatureProperties.properties() : properties;
		return this;
	}

	public Feature property(String key, String value) {
		this.properties.put(key, value);
		return this;
	}

	public Feature property(String key, Identifier value) {
		this.properties.put(key, value);
		return this;
	}

	public Feature property(String key, int value) {
		this.properties.put(key, value);
		return this;
	}

	public Feature property(String key, float value) {
		this.properties.put(key, value);
		return this;
	}

	public Feature property(String key, boolean value) {
		this.properties.put(key, value);
		return this;
	}

	public Feature property(String key, Object value) {
		this.properties.put(key, value);
		return this;
	}

	public <T> Feature property(String key, Codec<T> codec, T value) {
		this.properties.put(key, codec, value);
		return this;
	}

	public <T> Feature property(String key, List<T> values, Codec<T> codec) {
		this.properties.putList(key, values, codec);
		return this;
	}

	public String getType() {
		return type;
	}

	public FeatureProperties getProperties() {
		return properties;
	}
}
