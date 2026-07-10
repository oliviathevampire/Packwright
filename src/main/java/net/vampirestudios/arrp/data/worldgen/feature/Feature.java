package net.vampirestudios.arrp.data.worldgen.feature;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.minecraft.resources.Identifier;

import java.util.List;

public class Feature {
	public static final Codec<Feature> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<Feature, T>> decode(DynamicOps<T> ops, T input) {
			JsonElement element = new Dynamic<>(ops, input).convert(JsonOps.INSTANCE).getValue();
			if (!element.isJsonObject()) return DataResult.error(() -> "Feature must be an object");

			JsonObject object = element.getAsJsonObject();
			JsonElement typeElement = object.get("type");
			if (typeElement == null || !typeElement.isJsonPrimitive()) {
				return DataResult.error(() -> "Feature is missing type");
			}

			JsonObject properties = object.deepCopy();
			properties.remove("type");
			return DataResult.success(Pair.of(new Feature(typeElement.getAsString(), new FeatureProperties(properties)), input));
		}

		@Override
		public <T> DataResult<T> encode(Feature feature, DynamicOps<T> ops, T prefix) {
			JsonObject object = new JsonObject();
			if (feature.type != null) object.addProperty("type", feature.type);
			for (var entry : feature.properties.asJsonObject().entrySet()) {
				object.add(entry.getKey(), entry.getValue());
			}
			return DataResult.success(new Dynamic<>(JsonOps.INSTANCE, object).convert(ops).getValue());
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

	public Feature property(String key, JsonElement value) {
		this.properties.put(key, value);
		return this;
	}

	public Feature property(String key, List<? extends JsonElement> values) {
		this.properties.putElements(key, values);
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
