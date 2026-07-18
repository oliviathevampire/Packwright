package net.vampirestudios.packwright.data.registry.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JavaOps;
import net.minecraft.resources.Identifier;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * the {@code effects} property bag of an {@link net.vampirestudios.packwright.data.registry.Enchantment}:
 * a map of effect-component-type id to effect config, keyed by the 31 vanilla
 * {@code EnchantmentEffectComponents} ids. Mirrors the "type-keyed property bag" pattern used by
 * {@link net.vampirestudios.packwright.data.worldgen.feature.FeatureProperties} for open-ended,
 * per-component-type shapes; every id can also be set generically via {@link #put(String, Codec, Object)}.
 */
public final class EnchantmentEffects {
	public static final Codec<EnchantmentEffects> CODEC = Codec.PASSTHROUGH.comapFlatMap(
			dynamic -> {
				Object value = dynamic.convert(JavaOps.INSTANCE).getValue();
				if (!(value instanceof Map<?, ?> map)) return DataResult.error(() -> "Enchantment effects must be an object");
				Map<String, Object> out = new LinkedHashMap<>();
				map.forEach((k, v) -> out.put(String.valueOf(k), v));
				return DataResult.success(new EnchantmentEffects(out));
			},
			effects -> new Dynamic<>(JavaOps.INSTANCE, effects.values)
	);

	private final Map<String, Object> values;

	private EnchantmentEffects(Map<String, Object> values) {
		this.values = values;
	}

	public static EnchantmentEffects effects() {
		return new EnchantmentEffects(new LinkedHashMap<>());
	}

	/** the raw effect-id -> encoded-value map backing this object */
	public Map<String, Object> values() {
		return new LinkedHashMap<>(values);
	}

	public boolean isEmpty() {
		return values.isEmpty();
	}

	// ---------- generic escape hatch ----------

	/** sets an arbitrary component id to an already-typed value; use for anything not covered below */
	public <T> EnchantmentEffects put(String id, Codec<T> codec, T value) {
		values.put(id, codec.encodeStart(JavaOps.INSTANCE, value).getOrThrow());
		return this;
	}

	private static <T> Codec<List<ConditionalEffect<T>>> conditionalListCodec(Codec<T> effectCodec) {
		return ConditionalEffect.codec(effectCodec).listOf();
	}

	// ---------- the 31 EnchantmentEffectComponents ids ----------

	public EnchantmentEffects damageProtection(List<ConditionalEffect<EnchantmentValueEffect>> effects) {
		return put("damage_protection", conditionalListCodec(EnchantmentValueEffect.CODEC), effects);
	}

	public EnchantmentEffects damageImmunity(List<ConditionalEffect<DamageImmunity>> effects) {
		return put("damage_immunity", conditionalListCodec(DamageImmunity.CODEC), effects);
	}

	public EnchantmentEffects damage(List<ConditionalEffect<EnchantmentValueEffect>> effects) {
		return put("damage", conditionalListCodec(EnchantmentValueEffect.CODEC), effects);
	}

	public EnchantmentEffects smashDamagePerFallenBlock(List<ConditionalEffect<EnchantmentValueEffect>> effects) {
		return put("smash_damage_per_fallen_block", conditionalListCodec(EnchantmentValueEffect.CODEC), effects);
	}

	public EnchantmentEffects knockback(List<ConditionalEffect<EnchantmentValueEffect>> effects) {
		return put("knockback", conditionalListCodec(EnchantmentValueEffect.CODEC), effects);
	}

	public EnchantmentEffects armorEffectiveness(List<ConditionalEffect<EnchantmentValueEffect>> effects) {
		return put("armor_effectiveness", conditionalListCodec(EnchantmentValueEffect.CODEC), effects);
	}

	public EnchantmentEffects postAttack(List<TargetedConditionalEffect<EnchantmentEntityEffect>> effects) {
		return put("post_attack", TargetedConditionalEffect.codec(EnchantmentEntityEffect.CODEC).listOf(), effects);
	}

	public EnchantmentEffects postPiercingAttack(List<ConditionalEffect<EnchantmentEntityEffect>> effects) {
		return put("post_piercing_attack", conditionalListCodec(EnchantmentEntityEffect.CODEC), effects);
	}

	public EnchantmentEffects hitBlock(List<ConditionalEffect<EnchantmentEntityEffect>> effects) {
		return put("hit_block", conditionalListCodec(EnchantmentEntityEffect.CODEC), effects);
	}

	public EnchantmentEffects itemDamage(List<ConditionalEffect<EnchantmentValueEffect>> effects) {
		return put("item_damage", conditionalListCodec(EnchantmentValueEffect.CODEC), effects);
	}

	/** {@code affected} on each entry is always {@link EnchantmentTarget#VICTIM} and isn't serialized */
	public EnchantmentEffects equipmentDrops(List<TargetedConditionalEffect<EnchantmentValueEffect>> effects) {
		return put("equipment_drops", TargetedConditionalEffect.equipmentDropsCodec(EnchantmentValueEffect.CODEC).listOf(), effects);
	}

	public EnchantmentEffects locationChanged(List<ConditionalEffect<EnchantmentLocationBasedEffect>> effects) {
		return put("location_changed", conditionalListCodec(EnchantmentLocationBasedEffect.CODEC), effects);
	}

	public EnchantmentEffects tick(List<ConditionalEffect<EnchantmentEntityEffect>> effects) {
		return put("tick", conditionalListCodec(EnchantmentEntityEffect.CODEC), effects);
	}

	public EnchantmentEffects ammoUse(List<ConditionalEffect<EnchantmentValueEffect>> effects) {
		return put("ammo_use", conditionalListCodec(EnchantmentValueEffect.CODEC), effects);
	}

	public EnchantmentEffects projectilePiercing(List<ConditionalEffect<EnchantmentValueEffect>> effects) {
		return put("projectile_piercing", conditionalListCodec(EnchantmentValueEffect.CODEC), effects);
	}

	public EnchantmentEffects projectileSpawned(List<ConditionalEffect<EnchantmentEntityEffect>> effects) {
		return put("projectile_spawned", conditionalListCodec(EnchantmentEntityEffect.CODEC), effects);
	}

	public EnchantmentEffects projectileSpread(List<ConditionalEffect<EnchantmentValueEffect>> effects) {
		return put("projectile_spread", conditionalListCodec(EnchantmentValueEffect.CODEC), effects);
	}

	public EnchantmentEffects projectileCount(List<ConditionalEffect<EnchantmentValueEffect>> effects) {
		return put("projectile_count", conditionalListCodec(EnchantmentValueEffect.CODEC), effects);
	}

	public EnchantmentEffects tridentReturnAcceleration(List<ConditionalEffect<EnchantmentValueEffect>> effects) {
		return put("trident_return_acceleration", conditionalListCodec(EnchantmentValueEffect.CODEC), effects);
	}

	public EnchantmentEffects fishingTimeReduction(List<ConditionalEffect<EnchantmentValueEffect>> effects) {
		return put("fishing_time_reduction", conditionalListCodec(EnchantmentValueEffect.CODEC), effects);
	}

	public EnchantmentEffects fishingLuckBonus(List<ConditionalEffect<EnchantmentValueEffect>> effects) {
		return put("fishing_luck_bonus", conditionalListCodec(EnchantmentValueEffect.CODEC), effects);
	}

	public EnchantmentEffects blockExperience(List<ConditionalEffect<EnchantmentValueEffect>> effects) {
		return put("block_experience", conditionalListCodec(EnchantmentValueEffect.CODEC), effects);
	}

	public EnchantmentEffects mobExperience(List<ConditionalEffect<EnchantmentValueEffect>> effects) {
		return put("mob_experience", conditionalListCodec(EnchantmentValueEffect.CODEC), effects);
	}

	public EnchantmentEffects repairWithXp(List<ConditionalEffect<EnchantmentValueEffect>> effects) {
		return put("repair_with_xp", conditionalListCodec(EnchantmentValueEffect.CODEC), effects);
	}

	/** bare objects (no {@code type} key), unlike the other dispatched effect lists */
	public EnchantmentEffects attributes(List<EnchantmentAttributeEffect> effects) {
		return put("attributes", EnchantmentAttributeEffect.CODEC.listOf(), effects);
	}

	public EnchantmentEffects crossbowChargeTime(EnchantmentValueEffect effect) {
		return put("crossbow_charge_time", EnchantmentValueEffect.CODEC, effect);
	}

	public EnchantmentEffects crossbowChargingSounds(List<ChargingSounds> sounds) {
		return put("crossbow_charging_sounds", ChargingSounds.CODEC.listOf(), sounds);
	}

	public EnchantmentEffects tridentSound(List<Identifier> sounds) {
		return put("trident_sound", Identifier.CODEC.listOf(), sounds);
	}

	/** {@code Unit}-valued (no fields); encodes as {@code {}} */
	public EnchantmentEffects preventEquipmentDrop() {
		values.put("prevent_equipment_drop", new LinkedHashMap<String, Object>());
		return this;
	}

	/** {@code Unit}-valued (no fields); encodes as {@code {}} */
	public EnchantmentEffects preventArmorChange() {
		values.put("prevent_armor_change", new LinkedHashMap<String, Object>());
		return this;
	}

	public EnchantmentEffects tridentSpinAttackStrength(EnchantmentValueEffect effect) {
		return put("trident_spin_attack_strength", EnchantmentValueEffect.CODEC, effect);
	}
}
