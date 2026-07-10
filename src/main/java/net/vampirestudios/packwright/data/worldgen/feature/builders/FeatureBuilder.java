package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.vampirestudios.packwright.data.worldgen.feature.*;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;

public class FeatureBuilder {
	protected final Feature feature;

	public FeatureBuilder(String type) {
		this.feature = Feature.of(type);
	}

	public FeatureBuilder property(String key, JsonElement value) {
		feature.property(key, value);
		return this;
	}

	public FeatureBuilder property(String key, Object value) {
		feature.property(key, value);
		return this;
	}

	public FeatureBuilder property(String key, Identifier value) {
		feature.property(key, value);
		return this;
	}

	public <T> FeatureBuilder property(String key, Codec<T> codec, T value) {
		feature.property(key, codec, value);
		return this;
	}

	public Feature build() {
		return feature;
	}
}
