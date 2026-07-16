package net.vampirestudios.packwright.data.registry.enchantment;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

/** who a {@link TargetedConditionalEffect} (e.g. {@code post_attack}) is enchanted on / applied to */
public enum EnchantmentTarget implements StringRepresentable {
	ATTACKER("attacker"),
	DAMAGING_ENTITY("damaging_entity"),
	VICTIM("victim");

	public static final Codec<EnchantmentTarget> CODEC = StringRepresentable.fromEnum(EnchantmentTarget::values);
	private final String name;
	EnchantmentTarget(String name) { this.name = name; }
	@Override public String getSerializedName() { return name; }
}
