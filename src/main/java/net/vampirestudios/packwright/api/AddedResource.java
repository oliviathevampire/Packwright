package net.vampirestudios.packwright.api;

import net.minecraft.resources.Identifier;

public record AddedResource<T>(
        PackResourceKey<T> key,
        Identifier path,
        byte[] data
) {}