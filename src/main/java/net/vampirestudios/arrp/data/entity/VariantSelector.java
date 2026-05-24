package net.vampirestudios.arrp.data.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public final class VariantSelector {
	private static final Codec<JsonObject> JSON_OBJECT = new Codec<>() {
		@Override public <T> DataResult<T> encode(JsonObject v, DynamicOps<T> ops, T prefix) {
			return DataResult.success(new Dynamic<>(JsonOps.INSTANCE, v).convert(ops).getValue());
		}
		@Override public <T> DataResult<Pair<JsonObject, T>> decode(DynamicOps<T> ops, T input) {
			JsonElement el = new Dynamic<>(ops, input).convert(JsonOps.INSTANCE).getValue();
			if (!el.isJsonObject()) return DataResult.error(() -> "Expected object for VariantSelector condition");
			return DataResult.success(Pair.of(el.getAsJsonObject(), input));
		}
	};

	public static final Codec<VariantSelector> CODEC = RecordCodecBuilder.create(i -> i.group(
			JSON_OBJECT.optionalFieldOf("condition").forGetter(s -> Optional.ofNullable(s.condition)),
			Codec.INT.optionalFieldOf("priority").forGetter(s -> Optional.ofNullable(s.priority))
	).apply(i, (cond, pri) -> {
		VariantSelector s = new VariantSelector();
		s.condition = cond.orElse(null);
		s.priority = pri.orElse(null);
		return s;
	}));

	private JsonObject condition;
	private Integer priority;

	public static VariantSelector of(JsonObject condition, int priority) {
		VariantSelector s = new VariantSelector();
		s.condition = condition;
		s.priority = priority;
		return s;
	}

	public static VariantSelector fallback() {
		VariantSelector s = new VariantSelector();
		s.priority = 0;
		return s;
	}

	public VariantSelector condition(JsonObject condition) { this.condition = condition; return this; }
	public VariantSelector priority(int priority) { this.priority = priority; return this; }

	public JsonObject getCondition() { return condition; }
	public Integer getPriority() { return priority; }
}
