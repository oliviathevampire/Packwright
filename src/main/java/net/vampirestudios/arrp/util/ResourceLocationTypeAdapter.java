package net.vampirestudios.arrp.util;

import com.google.gson.*;
import net.minecraft.resources.Identifier;

import java.lang.reflect.Type;

/**
 * Serializes a ResourceLocation as one JSON string ("namespace:path"),
 * and deserializes back from that string.
 */
public class ResourceLocationTypeAdapter implements JsonSerializer<Identifier>, JsonDeserializer<Identifier> {

    @Override
    public JsonElement serialize(Identifier src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    @Override
    public Identifier deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonPrimitive() || !json.getAsJsonPrimitive().isString()) {
            throw new JsonParseException("Expected ResourceLocation as string");
        }
        return Identifier.tryParse(json.getAsString());
    }
}
