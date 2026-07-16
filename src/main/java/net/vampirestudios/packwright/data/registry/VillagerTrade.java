package net.vampirestudios.packwright.data.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.component.DataComponentPatch;
import net.vampirestudios.packwright.data.loot.Condition;
import net.vampirestudios.packwright.data.loot.LootFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VillagerTrade {
	public static final Codec<VillagerTrade> CODEC = RecordCodecBuilder.create(i -> i.group(
			TradeCost.CODEC.fieldOf("wants").forGetter(x -> x.wants),
			TradeCost.CODEC.optionalFieldOf("additional_wants").forGetter(x -> x.additionalWants),
			ItemStack.CODEC.fieldOf("gives").forGetter(x -> x.gives),
			TradeSet.NumberProvider.CODEC.fieldOf("max_uses").orElse(TradeSet.NumberProvider.constant(4)).forGetter(x -> x.maxUses),
			TradeSet.NumberProvider.CODEC.optionalFieldOf("reputation_discount", TradeSet.NumberProvider.constant(0)).forGetter(x -> x.reputationDiscount),
			TradeSet.NumberProvider.CODEC.fieldOf("xp").orElse(TradeSet.NumberProvider.constant(1)).forGetter(x -> x.xp),
			Condition.CODEC.optionalFieldOf("merchant_predicate").forGetter(x -> x.merchantPredicate),
			LootFunction.CODEC.listOf().optionalFieldOf("given_item_modifiers", List.of()).forGetter(x -> x.givenItemModifiers),
			Enchantments.CODEC.optionalFieldOf("double_trade_price_enchantments").forGetter(x -> x.doubleTradePriceEnchantments)
	).apply(i, (wants, additionalWants, gives, maxUses, reputationDiscount, xp, merchantPredicate, modifiers, doubleTradePriceEnchantments) -> new VillagerTrade()
			.wants(wants).additionalWants(additionalWants).gives(gives).maxUses(maxUses).reputationDiscount(reputationDiscount).xp(xp)
			.merchantPredicate(merchantPredicate).givenItemModifiers(modifiers).doubleTradePriceEnchantments(doubleTradePriceEnchantments)));

	private TradeCost wants = new TradeCost("minecraft:emerald", TradeSet.NumberProvider.constant(1));
	private Optional<TradeCost> additionalWants = Optional.empty();
	private ItemStack gives = new ItemStack("minecraft:stone", 1);
	private TradeSet.NumberProvider maxUses = TradeSet.NumberProvider.constant(4);
	private TradeSet.NumberProvider reputationDiscount = TradeSet.NumberProvider.constant(0);
	private TradeSet.NumberProvider xp = TradeSet.NumberProvider.constant(1);
	private Optional<Condition> merchantPredicate = Optional.empty();
	private List<LootFunction> givenItemModifiers = new ArrayList<>();
	private Optional<Enchantments> doubleTradePriceEnchantments = Optional.empty();

	public static VillagerTrade trade() { return new VillagerTrade(); }
	public VillagerTrade wants(TradeCost wants) { this.wants = wants; return this; }
	public VillagerTrade wants(String item, int count) { return wants(new TradeCost(item, TradeSet.NumberProvider.constant(count))); }
	public VillagerTrade additionalWants(Optional<TradeCost> additionalWants) { this.additionalWants = additionalWants; return this; }
	public VillagerTrade additionalWants(String item, int count) { this.additionalWants = Optional.of(new TradeCost(item, TradeSet.NumberProvider.constant(count))); return this; }
	public VillagerTrade gives(ItemStack gives) { this.gives = gives; return this; }
	public VillagerTrade gives(String item, int count) { return gives(new ItemStack(item, count)); }
	public VillagerTrade maxUses(TradeSet.NumberProvider maxUses) { this.maxUses = maxUses; return this; }
	public VillagerTrade maxUses(int maxUses) { return maxUses(TradeSet.NumberProvider.constant(maxUses)); }
	public VillagerTrade reputationDiscount(TradeSet.NumberProvider reputationDiscount) { this.reputationDiscount = reputationDiscount; return this; }
	public VillagerTrade xp(TradeSet.NumberProvider xp) { this.xp = xp; return this; }
	public VillagerTrade xp(int xp) { return xp(TradeSet.NumberProvider.constant(xp)); }
	public VillagerTrade merchantPredicate(Optional<Condition> merchantPredicate) { this.merchantPredicate = merchantPredicate; return this; }
	public VillagerTrade merchantPredicate(Condition merchantPredicate) { this.merchantPredicate = Optional.of(merchantPredicate); return this; }
	public VillagerTrade givenItemModifiers(List<LootFunction> modifiers) { this.givenItemModifiers = new ArrayList<>(modifiers); return this; }
	public VillagerTrade givenItemModifier(LootFunction modifier) { this.givenItemModifiers.add(modifier); return this; }
	/** enchantments (or a {@code #tag}) that, when present on {@link #gives}, double the trade's cost */
	public VillagerTrade doubleTradePriceEnchantments(Optional<Enchantments> enchantments) { this.doubleTradePriceEnchantments = enchantments; return this; }
	public VillagerTrade doubleTradePriceEnchantments(Enchantments enchantments) { this.doubleTradePriceEnchantments = Optional.of(enchantments); return this; }

	public record TradeCost(String id, TradeSet.NumberProvider count, Optional<DataComponentExactPredicate> components) {
		public static final Codec<TradeCost> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("id").forGetter(TradeCost::id),
				TradeSet.NumberProvider.CODEC.optionalFieldOf("count", TradeSet.NumberProvider.constant(1)).forGetter(TradeCost::count),
				DataComponentExactPredicate.CODEC.optionalFieldOf("components").forGetter(TradeCost::components)
		).apply(i, TradeCost::new));

		public TradeCost(String id, TradeSet.NumberProvider count) {
			this(id, count, Optional.empty());
		}

		public TradeCost withComponents(DataComponentExactPredicate components) {
			return new TradeCost(id, count, Optional.ofNullable(components));
		}
	}

	public record ItemStack(String id, int count, Optional<DataComponentPatch> components) {
		public static final Codec<ItemStack> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("id").forGetter(ItemStack::id),
				Codec.INT.optionalFieldOf("count", 1).forGetter(ItemStack::count),
				DataComponentPatch.CODEC.optionalFieldOf("components").forGetter(ItemStack::components)
		).apply(i, ItemStack::new));

		public ItemStack(String id, int count) {
			this(id, count, Optional.empty());
		}

		public ItemStack withComponents(DataComponentPatch components) {
			return new ItemStack(id, count, Optional.ofNullable(components));
		}
	}
}
