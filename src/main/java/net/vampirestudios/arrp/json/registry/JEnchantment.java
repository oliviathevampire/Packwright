package net.vampirestudios.arrp.json.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class JEnchantment {
	public static final Codec<JEnchantment> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.fieldOf("description").forGetter(x -> x.description),
			Codec.STRING.fieldOf("supported_items").forGetter(x -> x.supportedItems),
			Codec.STRING.optionalFieldOf("primary_items").forGetter(x -> Optional.ofNullable(x.primaryItems)),
			Codec.INT.fieldOf("weight").forGetter(x -> x.weight),
			Codec.INT.fieldOf("max_level").forGetter(x -> x.maxLevel),
			Cost.CODEC.fieldOf("min_cost").forGetter(x -> x.minCost),
			Cost.CODEC.fieldOf("max_cost").forGetter(x -> x.maxCost),
			Codec.INT.fieldOf("anvil_cost").forGetter(x -> x.anvilCost),
			EquipmentSlot.CODEC.listOf().fieldOf("slots").forGetter(x -> x.slots),
			Codec.STRING.optionalFieldOf("exclusive_set").forGetter(x -> Optional.ofNullable(x.exclusiveSet))
	).apply(i, (desc, sup, pri, weight, maxLevel, minCost, maxCost, anvilCost, slots, excl) -> {
		JEnchantment out = new JEnchantment();
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
	private String exclusiveSet;

	public static JEnchantment enchantment() {
		return new JEnchantment();
	}

	public JEnchantment description(String translationKey) { this.description = translationKey; return this; }
	public JEnchantment supportedItems(String itemOrTag) { this.supportedItems = itemOrTag; return this; }
	public JEnchantment primaryItems(String itemOrTag) { this.primaryItems = itemOrTag; return this; }
	public JEnchantment weight(int weight) { this.weight = weight; return this; }
	public JEnchantment maxLevel(int maxLevel) { this.maxLevel = maxLevel; return this; }
	public JEnchantment minCost(int base, int perLevelAboveFirst) { this.minCost = new Cost(base, perLevelAboveFirst); return this; }
	public JEnchantment maxCost(int base, int perLevelAboveFirst) { this.maxCost = new Cost(base, perLevelAboveFirst); return this; }
	public JEnchantment anvilCost(int anvilCost) { this.anvilCost = anvilCost; return this; }
	public JEnchantment slots(EquipmentSlot... slots) { this.slots = new ArrayList<>(Arrays.asList(slots)); return this; }
	public JEnchantment exclusiveSet(String tag) { this.exclusiveSet = tag; return this; }

	public String getDescription() { return description; }
	public String getSupportedItems() { return supportedItems; }
	public String getPrimaryItems() { return primaryItems; }
	public int getWeight() { return weight; }
	public int getMaxLevel() { return maxLevel; }
	public Cost getMinCost() { return minCost; }
	public Cost getMaxCost() { return maxCost; }
	public int getAnvilCost() { return anvilCost; }
	public List<EquipmentSlot> getSlots() { return slots; }
	public String getExclusiveSet() { return exclusiveSet; }

	public static class Cost {
		public static final Codec<Cost> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.INT.fieldOf("base").forGetter(x -> x.base),
				Codec.INT.fieldOf("per_level_above_first").forGetter(x -> x.perLevelAboveFirst)
		).apply(i, Cost::new));

		private final int base;
		private final int perLevelAboveFirst;

		public Cost(int base, int perLevelAboveFirst) {
			this.base = base;
			this.perLevelAboveFirst = perLevelAboveFirst;
		}

		public int getBase() { return base; }
		public int getPerLevelAboveFirst() { return perLevelAboveFirst; }
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
		ARMOR("armor");

		public static final Codec<EquipmentSlot> CODEC = StringRepresentable.fromEnum(EquipmentSlot::values);
		private final String name;
		EquipmentSlot(String name) { this.name = name; }
		@Override public String getSerializedName() { return name; }
	}
}
