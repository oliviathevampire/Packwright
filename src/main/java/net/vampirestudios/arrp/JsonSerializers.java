package net.vampirestudios.arrp;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.StringRepresentable;
import org.joml.Vector3f;

public final class JsonSerializers {
	public static final JsonSerializer<Either<?, ?>> EITHER = (src, type, context) -> src.map(context::serialize, context::serialize);
	public static final JsonSerializer<Vector3f> VECTOR_3F = (src, type, context) -> {
		final JsonArray array = new JsonArray();
		array.add(src.x);
		array.add(src.y);
		array.add(src.z);
		return array;
	};
	public static final JsonSerializer<StringRepresentable> STRING_IDENTIFIABLE = (src, type, context) -> new JsonPrimitive(src.getSerializedName());
	public static final JsonSerializer<Identifier> IDENTIFIER = (src, type, context) -> new JsonPrimitive(src.toString());

	private JsonSerializers() {
	}

	public static <T> JsonSerializer<T> forCodec(Codec<T> codec) {
		return (src, typeOfSrc, context) -> codec.encodeStart(JsonOps.INSTANCE, src).getOrThrow();
	}

	public static <T> JsonSerializer<T> forCodec(Codec<T> codec, HolderLookup.Provider registryLookup) {
		final RegistryOps<JsonElement> ops = registryLookup.createSerializationContext(JsonOps.INSTANCE);
		return (src, typeOfSrc, context) -> codec.encodeStart(ops, src).getOrThrow();
	}
}