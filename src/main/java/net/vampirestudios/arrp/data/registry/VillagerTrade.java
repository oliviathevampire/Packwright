package net.vampirestudios.arrp.data.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VillagerTrade {
	public static final Codec<VillagerTrade> CODEC = RecordCodecBuilder.create(i -> i.group(
			TradeCost.CODEC.fieldOf("wants").forGetter(x -> x.wants),
			TradeCost.CODEC.optionalFieldOf("additional_wants").forGetter(x -> x.additionalWants),
			ItemStack.CODEC.fieldOf("gives").forGetter(x -> x.gives),
			TradeSet.NumberProvider.CODEC.optionalFieldOf("max_uses", TradeSet.NumberProvider.constant(1)).forGetter(x -> x.maxUses),
			TradeSet.NumberProvider.CODEC.optionalFieldOf("reputation_discount", TradeSet.NumberProvider.constant(0)).forGetter(x -> x.reputationDiscount),
			TradeSet.NumberProvider.CODEC.optionalFieldOf("xp", TradeSet.NumberProvider.constant(0)).forGetter(x -> x.xp),
			Codec.STRING.optionalFieldOf("merchant_predicate").forGetter(x -> x.merchantPredicate),
			Codec.STRING.listOf().optionalFieldOf("given_item_modifiers", List.of()).forGetter(x -> x.givenItemModifiers)
	).apply(i, (wants, additionalWants, gives, maxUses, reputationDiscount, xp, merchantPredicate, modifiers) -> new VillagerTrade()
			.wants(wants).additionalWants(additionalWants).gives(gives).maxUses(maxUses).reputationDiscount(reputationDiscount).xp(xp).merchantPredicate(merchantPredicate).givenItemModifiers(modifiers)));

	private TradeCost wants = new TradeCost("minecraft:emerald", TradeSet.NumberProvider.constant(1));
	private Optional<TradeCost> additionalWants = Optional.empty();
	private ItemStack gives = new ItemStack("minecraft:stone", 1);
	private TradeSet.NumberProvider maxUses = TradeSet.NumberProvider.constant(1);
	private TradeSet.NumberProvider reputationDiscount = TradeSet.NumberProvider.constant(0);
	private TradeSet.NumberProvider xp = TradeSet.NumberProvider.constant(0);
	private Optional<String> merchantPredicate = Optional.empty();
	private List<String> givenItemModifiers = new ArrayList<>();

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
	public VillagerTrade merchantPredicate(Optional<String> merchantPredicate) { this.merchantPredicate = merchantPredicate; return this; }
	public VillagerTrade merchantPredicate(String merchantPredicate) { this.merchantPredicate = Optional.of(merchantPredicate); return this; }
	public VillagerTrade givenItemModifiers(List<String> modifiers) { this.givenItemModifiers = new ArrayList<>(modifiers); return this; }
	public VillagerTrade givenItemModifier(String modifier) { this.givenItemModifiers.add(modifier); return this; }

	public record TradeCost(String id, TradeSet.NumberProvider count) {
		public static final Codec<TradeCost> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("id").forGetter(TradeCost::id),
				TradeSet.NumberProvider.CODEC.optionalFieldOf("count", TradeSet.NumberProvider.constant(1)).forGetter(TradeCost::count)
		).apply(i, TradeCost::new));
	}

	public record ItemStack(String id, int count) {
		public static final Codec<ItemStack> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("id").forGetter(ItemStack::id),
				Codec.INT.optionalFieldOf("count", 1).forGetter(ItemStack::count)
		).apply(i, ItemStack::new));
	}
}
