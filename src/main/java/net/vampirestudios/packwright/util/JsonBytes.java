// JsonBytes.java
package net.vampirestudios.packwright.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;

import java.nio.charset.StandardCharsets;

public final class JsonBytes {
	public static final Gson GSON_COMPACT = new GsonBuilder().disableHtmlEscaping().create();
	public static final Gson GSON_PRETTY  = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

	private JsonBytes() {}

	public static <T> JsonElement toJsonElement(Codec<T> codec, T value) {
		return codec.encodeStart(JsonOps.INSTANCE, value)
				.getOrThrow();
	}

	public static <T> T fromJsonElement(Codec<T> codec, JsonElement el) {
		return codec.parse(JsonOps.INSTANCE, el)
				.getOrThrow();
	}

	public static <T> String encodeToString(Codec<T> codec, T value) {
		return toJsonElement(codec, value).toString();
	}

	public static <T> String encodeToPrettyString(Codec<T> codec, T value) {
		return GSON_PRETTY.toJson(toJsonElement(codec, value));
	}

	public static <T> T parseFromString(Codec<T> codec, String json) {
		return fromJsonElement(codec, JsonParser.parseString(json));
	}

	public static <T> byte[] encodeToBytes(Codec<T> codec, T value) {
		JsonElement el = codec.encodeStart(JsonOps.INSTANCE, value).getOrThrow();
		return toBytes(el);
	}

	public static <T> byte[] encodeToPrettyBytes(Codec<T> codec, T value) {
		JsonElement el = codec.encodeStart(JsonOps.INSTANCE, value).getOrThrow();
		return toPrettyBytes(el);
	}

	public static byte[] toBytes(JsonElement el) {
		return el.toString().getBytes(StandardCharsets.UTF_8);
	}

	public static byte[] toPrettyBytes(JsonElement el) {
		String json = GSON_PRETTY.toJson(el);
		return json.getBytes(StandardCharsets.UTF_8);
	}
}
