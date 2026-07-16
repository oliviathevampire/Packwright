package net.vampirestudios.packwright.data.registry.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.JavaOps;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.predicate.PredicateBuilder;

/**
 * An entity-targeting enchantment effect (e.g. {@code minecraft:ignite}, {@code minecraft:damage_entity}),
 * used by {@code post_attack}, {@code hit_block}, {@code tick}, {@code post_piercing_attack} and
 * {@code projectile_spawned}. This is a deliberately minimal free-form builder — like
 * {@link net.vampirestudios.packwright.data.loot.Condition}/{@link net.vampirestudios.packwright.data.loot.LootFunction},
 * it models the common {@code type} + parameters shape rather than typing every one of vanilla's
 * ~15 effect kinds (ignite, damage_entity, explode, play_sound, replace_block, replace_disk,
 * run_function, set_block_properties, spawn_particles, summon_entity, apply_mob_effect,
 * apply_impulse, apply_exhaustion, change_item_damage, all_of).
 */
public class EnchantmentEntityEffect extends PredicateBuilder<EnchantmentEntityEffect> {
	public static final Codec<EnchantmentEntityEffect> CODEC = codecOf(EnchantmentEntityEffect::new, "type", "Enchantment entity effect");

	/** an entity effect of the given type, e.g. {@code "minecraft:ignite"} or a modded id */
	public static EnchantmentEntityEffect of(String type) {
		EnchantmentEntityEffect effect = new EnchantmentEntityEffect();
		return effect.type(type);
	}

	public static EnchantmentEntityEffect of(Identifier type) {
		return of(type.toString());
	}

	public static EnchantmentEntityEffect ignite(int duration) {
		return of("minecraft:ignite").parameter("duration", duration);
	}

	public static EnchantmentEntityEffect damageEntity(String damageType, EnchantmentValueEffect amount) {
		return of("minecraft:damage_entity").parameter("damage_type", damageType).parameter("amount", amount);
	}

	public EnchantmentEntityEffect type(String type) {
		return parameter("type", type);
	}

	/** sets a free-form {@link EnchantmentValueEffect} parameter, e.g. {@code amount} on {@code damage_entity} */
	public EnchantmentEntityEffect parameter(String key, EnchantmentValueEffect value) {
		return put(key, EnchantmentValueEffect.CODEC.encodeStart(JavaOps.INSTANCE, value).getOrThrow());
	}
}
