package net.vampirestudios.arrp.util;

import com.google.gson.*;
import java.lang.reflect.Type;
import net.minecraft.resources.Identifier;

/**
 * Serializes a ResourceLocation as one JSON string ("namespace:path"),
 * and deserializes back from that string.
 */
public class ResourceLocationTypeAdapter implements JsonSerializer<Identifier>, JsonDeserializer<Identifier> {

    @Override
    public JsonElement serialize(Identifier src, Type typeOfSrc, JsonSerializationContext context) {
        // simply output "namespace:path"
        return new JsonPrimitive(src.toString());
    }

    @Override
    public Identifier deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        // read "namespace:path"
        if (!json.isJsonPrimitive() || !json.getAsJsonPrimitive().isString()) {
            throw new JsonParseException("Expected ResourceLocation as string");
        }
        return Identifier.tryParse(json.getAsString());
    }
}
