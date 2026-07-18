package net.vampirestudios.packwright.data.registry.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

/** the empty-object effect payload used by {@code damage_immunity} */
public record DamageImmunity() {
	public static final DamageImmunity INSTANCE = new DamageImmunity();
	public static final Codec<DamageImmunity> CODEC = MapCodec.unit(INSTANCE).codec();
}
