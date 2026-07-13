package net.vampirestudios.packwright.data.worldgen.material;

import net.minecraft.resources.Identifier;

/**
 * A reference to an entry in the
 * {@code worldgen/material_condition} registry.
 *
 * <p>This serializes directly as an identifier rather than as a typed
 * object.</p>
 */
public record ReferenceMaterialCondition(
		Identifier id
) implements MaterialCondition {
}