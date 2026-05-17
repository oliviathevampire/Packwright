package net.vampirestudios.arrp.data.worldgen;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;

/**
 * Opaque "any JSON value" used for environment attributes.
 *
 * Backed by Codec.PASSTHROUGH, so it can represent:
 *  - primitives (bool/number/string)
 *  - arrays
 *  - objects
 *
 * You can still create them via typed helpers (boolean, double, String, or any Codec<T>).
 */
public record AttributeValue(Dynamic<?> value) {

	/**
	 * Serialized as "whatever JSON is inside".
	 *
	 * Example JSON:
	 *   true
	 *   3.14
	 *   "hello"
	 *   { "max_delay": 24000, "sound": "minecraft:music.overworld" }
	 *   [ { "particle": "minecraft:ash" }, { "particle": "minecraft:warp_spore" } ]
	 */
	public static final Codec<AttributeValue> CODEC =
			Codec.PASSTHROUGH.xmap(AttributeValue::new, AttributeValue::raw);

	// ---------- Typed constructors (helpers) ----------

	public static AttributeValue ofBoolean(boolean v) {
		return ofEncoded(Codec.BOOL, v);
	}

	public static AttributeValue ofDouble(double v) {
		return ofEncoded(Codec.DOUBLE, v);
	}

	public static AttributeValue ofFloat(float v) {
		return ofEncoded(Codec.FLOAT, v);
	}

	public static AttributeValue ofInt(int v) {
		return ofEncoded(Codec.INT, v);
	}

	public static AttributeValue ofString(String v) {
		return ofEncoded(Codec.STRING, v);
	}

	/**
	 * Generic constructor: encode a value with a codec into JSON
	 * and wrap it as a AttributeValue.
	 */
	public static <T> AttributeValue ofEncoded(Codec<T> codec, T value) {
		DataResult<JsonElement> encoded = codec.encodeStart(JsonOps.INSTANCE, value);
		JsonElement raw = encoded.getOrThrow(msg -> {
			throw new IllegalArgumentException("Failed to encode attribute value: " + msg);
		});
		return new AttributeValue(new Dynamic<>(JsonOps.INSTANCE, raw));
	}

	/** Raw dynamic value (for advanced use). */
	public Dynamic<?> raw() {
		return value;
	}

	// ---------- Codec for AttributeValue itself ----------

	/**
	 * Try to decode this attribute back into a type using the given codec.
	 * Throws if decoding fails.
	 */
	public <T> T decode(Codec<T> codec) {
		DataResult<T> result = codec.parse(this.value);
		return result.getOrThrow(msg -> {
			throw new IllegalStateException("Failed to decode attribute value: " + msg);
		});
	}
}
