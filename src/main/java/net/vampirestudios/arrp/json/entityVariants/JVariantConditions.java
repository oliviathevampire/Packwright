package net.vampirestudios.arrp.json.entityVariants;

import com.google.gson.JsonObject;
import net.minecraft.resources.Identifier;

/**
 * Helpers for common selector conditions.
 * Keep this thin/extensible so new rule types don't require refactors.
 */
public final class JVariantConditions {
    private JVariantConditions() {}

    /** type: minecraft:biome, field: biomes (id, list, or tag in vanilla). */
    public static JsonObject biome(String biomeOrTagId) {
        JsonObject o = new JsonObject();
        o.addProperty("type", "minecraft:biome");
        o.addProperty("biomes", biomeOrTagId);
        return o;
    }

    public static JsonObject biome(Identifier biomeId) {
        return biome(biomeId.toString());
    }

    /** type: minecraft:structures, field: structures (id/list/tag in vanilla). */
    public static JsonObject structures(String structureOrTagId) {
        JsonObject o = new JsonObject();
        o.addProperty("type", "minecraft:structures");
        o.addProperty("structures", structureOrTagId);
        return o;
    }

    public static JsonObject structures(Identifier structureId) {
        return structures(structureId.toString());
    }

    /**
     * Escape hatch for rule types you don't want to model yet:
     * build your own JsonObject and pass it into JVariantSelector.
     */
    public static JsonObject raw(String type) {
        JsonObject o = new JsonObject();
        o.addProperty("type", type);
        return o;
    }
}
