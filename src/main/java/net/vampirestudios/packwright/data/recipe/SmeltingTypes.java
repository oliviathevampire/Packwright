package net.vampirestudios.packwright.data.recipe;

/**
 * Named type identifiers for cooking recipes.
 * Use {@link #getTypeId()} to obtain the string form for {@link Recipe#category(String)}.
 */
public enum SmeltingTypes {
    BLASTING("minecraft:blasting"),
    CAMPFIRE_COOKING("minecraft:campfire_cooking"),
    SMELTING("minecraft:smelting"),
    SMOKING("minecraft:smoking");

    private final String typeId;

    SmeltingTypes(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeId() {
        return typeId;
    }

    @Override
    public String toString() {
        return typeId;
    }
}
