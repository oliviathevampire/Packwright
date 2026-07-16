package net.vampirestudios.packwright.data.recipe;

/**
 * Named recipe-type identifiers for the cooking recipe types (as opposed to their
 * recipe-book <em>category</em>, which is {@link CookingBookCategory}). Currently unused
 * internally; kept as a convenience for referring to a cooking recipe type by name.
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
