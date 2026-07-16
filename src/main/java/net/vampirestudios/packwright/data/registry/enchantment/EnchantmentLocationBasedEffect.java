package net.vampirestudios.packwright.data.registry.enchantment;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.predicate.PredicateBuilder;

/**
 * A location-based enchantment effect (activates/deactivates as the wearer moves), used by the
 * {@code location_changed} effect component. Like {@link EnchantmentEntityEffect}, this is a
 * deliberately minimal free-form builder covering the common {@code type} + parameters shape
 * rather than typing every vanilla effect kind. Note: vanilla's {@code minecraft:attribute}
 * location-based effect type wraps an {@link EnchantmentAttributeEffect} — for the common case of
 * a plain attribute modifier, prefer {@link EnchantmentEffects#attributes} instead.
 */
public class EnchantmentLocationBasedEffect extends PredicateBuilder<EnchantmentLocationBasedEffect> {
	public static final Codec<EnchantmentLocationBasedEffect> CODEC = codecOf(EnchantmentLocationBasedEffect::new, "type", "Enchantment location-based effect");

	/** a location-based effect of the given type, e.g. {@code "minecraft:ignite"} or a modded id */
	public static EnchantmentLocationBasedEffect of(String type) {
		EnchantmentLocationBasedEffect effect = new EnchantmentLocationBasedEffect();
		return effect.type(type);
	}

	public static EnchantmentLocationBasedEffect of(Identifier type) {
		return of(type.toString());
	}

	public EnchantmentLocationBasedEffect type(String type) {
		return parameter("type", type);
	}
}
