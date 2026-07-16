package net.vampirestudios.packwright.data.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;
import net.vampirestudios.packwright.data.registry.enchantment.EnchantmentEffects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Enchantment {
	public static final Codec<Enchantment> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.fieldOf("description").forGetter(x -> x.description),
			Codec.STRING.fieldOf("supported_items").forGetter(x -> x.supportedItems),
			Codec.STRING.optionalFieldOf("primary_items").forGetter(x -> Optional.ofNullable(x.primaryItems)),
			Codec.INT.fieldOf("weight").forGetter(x -> x.weight),
			Codec.INT.fieldOf("max_level").forGetter(x -> x.maxLevel),
			Cost.CODEC.fieldOf("min_cost").forGetter(x -> x.minCost),
			Cost.CODEC.fieldOf("max_cost").forGetter(x -> x.maxCost),
			Codec.INT.fieldOf("anvil_cost").forGetter(x -> x.anvilCost),
			EquipmentSlot.CODEC.listOf().fieldOf("slots").forGetter(x -> x.slots),
			Enchantments.CODEC.optionalFieldOf("exclusive_set").forGetter(x -> Optional.ofNullable(x.exclusiveSet)),
			EnchantmentEffects.CODEC.fieldOf("effects").orElse(EnchantmentEffects.effects()).forGetter(x -> x.effects)
	).apply(i, (desc, sup, pri, weight, maxLevel, minCost, maxCost, anvilCost, slots, excl, effects) -> {
		Enchantment out = new Enchantment();
		out.description = desc;
		out.supportedItems = sup;
		out.primaryItems = pri.orElse(null);
		out.weight = weight;
		out.maxLevel = maxLevel;
		out.minCost = minCost;
		out.maxCost = maxCost;
		out.anvilCost = anvilCost;
		out.slots = new ArrayList<>(slots);
		out.exclusiveSet = excl.orElse(null);
		out.effects = effects;
		return out;
	}));

	private String description;
	private String supportedItems;
	private String primaryItems;
	private int weight = 1;
	private int maxLevel = 1;
	private Cost minCost;
	private Cost maxCost;
	private int anvilCost = 1;
	private List<EquipmentSlot> slots = new ArrayList<>();
	private Enchantments exclusiveSet;
	private EnchantmentEffects effects = EnchantmentEffects.effects();

	public static Enchantment enchantment() {
		return new Enchantment();
	}

	public Enchantment description(String translationKey) { this.description = translationKey; return this; }
	public Enchantment supportedItems(String itemOrTag) { this.supportedItems = itemOrTag; return this; }
	public Enchantment primaryItems(String itemOrTag) { this.primaryItems = itemOrTag; return this; }
	public Enchantment weight(int weight) { this.weight = weight; return this; }
	public Enchantment maxLevel(int maxLevel) { this.maxLevel = maxLevel; return this; }
	public Enchantment minCost(int base, int perLevelAboveFirst) { this.minCost = new Cost(base, perLevelAboveFirst); return this; }
	public Enchantment maxCost(int base, int perLevelAboveFirst) { this.maxCost = new Cost(base, perLevelAboveFirst); return this; }
	public Enchantment anvilCost(int anvilCost) { this.anvilCost = anvilCost; return this; }
	public Enchantment slots(EquipmentSlot... slots) { this.slots = new ArrayList<>(Arrays.asList(slots)); return this; }
	/** a tag, a single enchantment id, or an explicit list of enchantment ids that are incompatible with this one */
	public Enchantment exclusiveSet(Enchantments exclusiveSet) { this.exclusiveSet = exclusiveSet; return this; }
	public Enchantment effects(EnchantmentEffects effects) { this.effects = effects == null ? EnchantmentEffects.effects() : effects; return this; }

	public String getDescription() { return description; }
	public String getSupportedItems() { return supportedItems; }
	public String getPrimaryItems() { return primaryItems; }
	public int getWeight() { return weight; }
	public int getMaxLevel() { return maxLevel; }
	public Cost getMinCost() { return minCost; }
	public Cost getMaxCost() { return maxCost; }
	public int getAnvilCost() { return anvilCost; }
	public List<EquipmentSlot> getSlots() { return slots; }
	public Enchantments getExclusiveSet() { return exclusiveSet; }
	public EnchantmentEffects getEffects() { return effects; }

	public record Cost(int base, int perLevelAboveFirst) {
			public static final Codec<Cost> CODEC = RecordCodecBuilder.create(i -> i.group(
					Codec.INT.fieldOf("base").forGetter(x -> x.base),
					Codec.INT.fieldOf("per_level_above_first").forGetter(x -> x.perLevelAboveFirst)
			).apply(i, Cost::new));
	}

	public enum EquipmentSlot implements StringRepresentable {
		MAINHAND("mainhand"),
		OFFHAND("offhand"),
		HEAD("head"),
		CHEST("chest"),
		LEGS("legs"),
		FEET("feet"),
		BODY("body"),
		ANY("any"),
		HAND("hand"),
		ARMOR("armor"),
		SADDLE("saddle");

		public static final Codec<EquipmentSlot> CODEC = StringRepresentable.fromEnum(EquipmentSlot::values);
		private final String name;
		EquipmentSlot(String name) { this.name = name; }
		@Override public String getSerializedName() { return name; }
	}
}
