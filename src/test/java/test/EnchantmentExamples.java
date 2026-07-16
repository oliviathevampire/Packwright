package test;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.api.RuntimeResourcePack;
import net.vampirestudios.packwright.data.loot.Condition;
import net.vampirestudios.packwright.data.loot.EntityTarget;
import net.vampirestudios.packwright.data.registry.Enchantment;
import net.vampirestudios.packwright.data.registry.Enchantments;
import net.vampirestudios.packwright.data.registry.enchantment.ChargingSounds;
import net.vampirestudios.packwright.data.registry.enchantment.ConditionalEffect;
import net.vampirestudios.packwright.data.registry.enchantment.EnchantmentAttributeEffect;
import net.vampirestudios.packwright.data.registry.enchantment.EnchantmentEffects;
import net.vampirestudios.packwright.data.registry.enchantment.EnchantmentEntityEffect;
import net.vampirestudios.packwright.data.registry.enchantment.EnchantmentTarget;
import net.vampirestudios.packwright.data.registry.enchantment.EnchantmentValueEffect;
import net.vampirestudios.packwright.data.registry.enchantment.LevelBasedValue;
import net.vampirestudios.packwright.data.registry.enchantment.TargetedConditionalEffect;
import net.vampirestudios.packwright.data.predicate.EntityPredicate;
import net.vampirestudios.packwright.util.JsonBytes;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static net.vampirestudios.packwright.util.ResourceHelpers.customId;

/**
 * Examples for the {@code enchantment} registry's {@code effects} components (26.3): value
 * effects, targeted/conditional wrapping, attribute modifiers, and a couple of the unit-valued
 * "flag" effects.
 */
public class EnchantmentExamples {

	private static Identifier myModId(String path) {
		return customId("mymod", path);
	}

	private static Identifier vanillaId(String path) {
		return Identifier.withDefaultNamespace(path);
	}

	/** a sword enchant: scaling damage bonus, knockback, an attribute modifier, and an on-hit ignite */
	public static Enchantment buildEmberEdgeEnchantment() {
		return Enchantment.enchantment()
				.description("enchantment.mymod.ember_edge")
				.supportedItems("#minecraft:enchantable/sword")
				.weight(2)
				.maxLevel(3)
				.minCost(15, 9)
				.maxCost(65, 9)
				.anvilCost(4)
				.slots(Enchantment.EquipmentSlot.MAINHAND)
				.exclusiveSet(Enchantments.enchantment(vanillaId("fire_aspect")))
				.effects(EnchantmentEffects.effects()
						.damage(List.of(ConditionalEffect.of(
								EnchantmentValueEffect.add(LevelBasedValue.linear(1.0F, 1.0F))
						)))
						.knockback(List.of(ConditionalEffect.of(
								EnchantmentValueEffect.add(LevelBasedValue.constant(0.5F))
						)))
						.attributes(List.of(new EnchantmentAttributeEffect(
								myModId("ember_edge_attack_speed"),
								vanillaId("attack_speed"),
								LevelBasedValue.linear(0.05F, 0.05F),
								EnchantmentAttributeEffect.Operation.ADD_MULTIPLIED_TOTAL
						)))
						.postAttack(List.of(TargetedConditionalEffect.of(
								EnchantmentTarget.ATTACKER,
								EnchantmentTarget.VICTIM,
								EnchantmentEntityEffect.ignite(3),
								// only ignite if the victim isn't already ablaze, keeps repeated hits from stacking fire time forever
								Condition.entityProperties(EntityTarget.THIS, EntityPredicate.of().onFire(false))
						))));
	}

	/** a crossbow enchant: faster charging, a matching sound, and one extra bolt at higher levels */
	public static Enchantment buildQuickLoaderEnchantment() {
		return Enchantment.enchantment()
				.description("enchantment.mymod.quick_loader")
				.supportedItems(vanillaId("crossbow").toString())
				.weight(1)
				.maxLevel(2)
				.minCost(20, 10)
				.maxCost(50, 10)
				.anvilCost(6)
				.slots(Enchantment.EquipmentSlot.MAINHAND, Enchantment.EquipmentSlot.OFFHAND)
				.effects(EnchantmentEffects.effects()
						.crossbowChargeTime(EnchantmentValueEffect.add(LevelBasedValue.clamped(
								LevelBasedValue.linear(-0.15F, -0.15F), -0.4F, 0.0F
						)))
						.crossbowChargingSounds(List.of(
								new ChargingSounds(
										Optional.of(vanillaId("item.crossbow.quick_charge_1")),
										Optional.of(vanillaId("item.crossbow.quick_charge_2")),
										Optional.of(vanillaId("item.crossbow.quick_charge_3"))
								)
						))
						.projectileCount(List.of(ConditionalEffect.of(
								EnchantmentValueEffect.add(LevelBasedValue.linear(0.0F, 1.0F))
						))));
	}

	/** a curse: two of the {@code Unit}-valued "flag" effects, which just mark a behavior on/off with no config */
	public static Enchantment buildCurseOfClingingEnchantment() {
		return Enchantment.enchantment()
				.description("enchantment.mymod.curse_of_clinging")
				.supportedItems("#minecraft:enchantable/equippable")
				.weight(1)
				.maxLevel(1)
				.minCost(25, 0)
				.maxCost(50, 0)
				.anvilCost(4)
				.slots(Enchantment.EquipmentSlot.ARMOR)
				.effects(EnchantmentEffects.effects()
						.preventEquipmentDrop()
						.preventArmorChange());
	}

	public static void registerAll(RuntimeResourcePack pack) {
		pack.addEnchantment(myModId("ember_edge"), buildEmberEdgeEnchantment());
		pack.addEnchantment(myModId("quick_loader"), buildQuickLoaderEnchantment());
		pack.addEnchantment(myModId("curse_of_clinging"), buildCurseOfClingingEnchantment());
	}

	public static void main() {
		System.out.println("Enchantment JSON (mymod:ember_edge):");
		System.out.println(JsonBytes.encodeToPrettyString(Enchantment.CODEC, buildEmberEdgeEnchantment()));
		System.out.println("Enchantment JSON (mymod:quick_loader):");
		System.out.println(JsonBytes.encodeToPrettyString(Enchantment.CODEC, buildQuickLoaderEnchantment()));
		System.out.println("Enchantment JSON (mymod:curse_of_clinging):");
		System.out.println(JsonBytes.encodeToPrettyString(Enchantment.CODEC, buildCurseOfClingingEnchantment()));

		RuntimeResourcePack pack = RuntimeResourcePack.create("mymod:enchantment_examples");
		pack.addDataPackMcmeta("Enchantment effects examples generated by Packwright");
		registerAll(pack);
		pack.dumpDirect(Path.of("dumps/enchantment_examples"));
	}
}
