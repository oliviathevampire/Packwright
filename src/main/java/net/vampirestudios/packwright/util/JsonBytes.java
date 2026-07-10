// JsonBytes.java
package net.vampirestudios.packwright.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public final class JsonBytes {
	private static final Gson GSON_COMPACT = new GsonBuilder().disableHtmlEscaping().create();
	private static final Gson GSON_PRETTY  = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	private JsonBytes() {}

	// ------ Element helpers ------
	public static <T> JsonElement toJsonElement(Codec<T> codec, T value) {
		return codec.encodeStart(JsonOps.INSTANCE, value)
				.getOrThrow();
	}
	public static <T> T fromJsonElement(Codec<T> codec, JsonElement el) {
		return codec.parse(JsonOps.INSTANCE, el)
				.getOrThrow();
	}

	// ------ String helpers ------
	public static <T> String encodeToString(Codec<T> codec, T value) {
		return toJsonElement(codec, value).toString(); // compact
	}
	public static <T> String encodeToPrettyString(Codec<T> codec, T value) {
		return GSON_PRETTY.toJson(toJsonElement(codec, value)); // pretty
	}
	public static <T> T parseFromString(Codec<T> codec, String json) {
		return fromJsonElement(codec, JsonParser.parseString(json));
	}

	/** Encode using a Mojang Codec, then UTF-8 bytes (compact). */
	public static <T> byte[] encodeToBytes(Codec<T> codec, T value) {
		JsonElement el = codec.encodeStart(JsonOps.INSTANCE, value).getOrThrow();
		return toBytes(el);
	}

	/** Encode using a Mojang Codec, then pretty JSON bytes. */
	public static <T> byte[] encodeToPrettyBytes(Codec<T> codec, T value) {
		JsonElement el = codec.encodeStart(JsonOps.INSTANCE, value).getOrThrow();
		return toPrettyBytes(el);
	}

	/** JsonElement → compact bytes. */
	public static byte[] toBytes(JsonElement el) {
		return el.toString().getBytes(StandardCharsets.UTF_8);
	}

	/** JsonElement → pretty bytes. */
	public static byte[] toPrettyBytes(JsonElement el) {
		String json = GSON_PRETTY.toJson(el);
		return json.getBytes(StandardCharsets.UTF_8);
	}

	private static Consumer<String> fail(String phase) {
		return msg -> {
			throw new IllegalStateException("Codec " + phase + " failure: " + msg);
		};
	}
}
