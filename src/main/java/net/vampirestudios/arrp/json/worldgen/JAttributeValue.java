package net.vampirestudios.arrp.json.worldgen;

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
public record JAttributeValue(Dynamic<?> value) {

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
	public static final Codec<JAttributeValue> CODEC =
			Codec.PASSTHROUGH.xmap(JAttributeValue::new, JAttributeValue::raw);

	// ---------- Typed constructors (helpers) ----------

	public static JAttributeValue ofBoolean(boolean v) {
		return ofEncoded(Codec.BOOL, v);
	}

	public static JAttributeValue ofDouble(double v) {
		return ofEncoded(Codec.DOUBLE, v);
	}

	public static JAttributeValue ofFloat(float v) {
		return ofEncoded(Codec.FLOAT, v);
	}

	public static JAttributeValue ofInt(int v) {
		return ofEncoded(Codec.INT, v);
	}

	public static JAttributeValue ofString(String v) {
		return ofEncoded(Codec.STRING, v);
	}

	/**
	 * Generic constructor: encode a value with a codec into JSON
	 * and wrap it as a JAttributeValue.
	 */
	public static <T> JAttributeValue ofEncoded(Codec<T> codec, T value) {
		DataResult<JsonElement> encoded = codec.encodeStart(JsonOps.INSTANCE, value);
		JsonElement raw = encoded.getOrThrow(msg -> {
			throw new IllegalArgumentException("Failed to encode attribute value: " + msg);
		});
		return new JAttributeValue(new Dynamic<>(JsonOps.INSTANCE, raw));
	}

	/** Raw dynamic value (for advanced use). */
	public Dynamic<?> raw() {
		return value;
	}

	// ---------- Codec for JAttributeValue itself ----------

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
