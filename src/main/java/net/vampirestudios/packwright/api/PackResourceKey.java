package net.vampirestudios.packwright.api;

import net.minecraft.resources.Identifier;

public record PackResourceKey<T>(
        ResourceType<T> type,
        Identifier id
) {
    public static <T> PackResourceKey<T> of(
            ResourceType<T> type,
            Identifier id
    ) {
        return new PackResourceKey<>(type, id);
    }
}