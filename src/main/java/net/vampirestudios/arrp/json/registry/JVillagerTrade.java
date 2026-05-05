package net.vampirestudios.arrp.json.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JVillagerTrade {
	public static final Codec<JVillagerTrade> CODEC = RecordCodecBuilder.create(i -> i.group(
			TradeCost.CODEC.fieldOf("wants").forGetter(x -> x.wants),
			TradeCost.CODEC.optionalFieldOf("additional_wants").forGetter(x -> x.additionalWants),
			ItemStack.CODEC.fieldOf("gives").forGetter(x -> x.gives),
			JTradeSet.NumberProvider.CODEC.optionalFieldOf("max_uses", JTradeSet.NumberProvider.constant(1)).forGetter(x -> x.maxUses),
			JTradeSet.NumberProvider.CODEC.optionalFieldOf("reputation_discount", JTradeSet.NumberProvider.constant(0)).forGetter(x -> x.reputationDiscount),
			JTradeSet.NumberProvider.CODEC.optionalFieldOf("xp", JTradeSet.NumberProvider.constant(0)).forGetter(x -> x.xp),
			Codec.STRING.optionalFieldOf("merchant_predicate").forGetter(x -> x.merchantPredicate),
			Codec.STRING.listOf().optionalFieldOf("given_item_modifiers", List.of()).forGetter(x -> x.givenItemModifiers)
	).apply(i, (wants, additionalWants, gives, maxUses, reputationDiscount, xp, merchantPredicate, modifiers) -> new JVillagerTrade()
			.wants(wants).additionalWants(additionalWants).gives(gives).maxUses(maxUses).reputationDiscount(reputationDiscount).xp(xp).merchantPredicate(merchantPredicate).givenItemModifiers(modifiers)));

	private TradeCost wants = new TradeCost("minecraft:emerald", JTradeSet.NumberProvider.constant(1));
	private Optional<TradeCost> additionalWants = Optional.empty();
	private ItemStack gives = new ItemStack("minecraft:stone", 1);
	private JTradeSet.NumberProvider maxUses = JTradeSet.NumberProvider.constant(1);
	private JTradeSet.NumberProvider reputationDiscount = JTradeSet.NumberProvider.constant(0);
	private JTradeSet.NumberProvider xp = JTradeSet.NumberProvider.constant(0);
	private Optional<String> merchantPredicate = Optional.empty();
	private List<String> givenItemModifiers = new ArrayList<>();

	public static JVillagerTrade trade() { return new JVillagerTrade(); }
	public JVillagerTrade wants(TradeCost wants) { this.wants = wants; return this; }
	public JVillagerTrade wants(String item, int count) { return wants(new TradeCost(item, JTradeSet.NumberProvider.constant(count))); }
	public JVillagerTrade additionalWants(Optional<TradeCost> additionalWants) { this.additionalWants = additionalWants; return this; }
	public JVillagerTrade additionalWants(String item, int count) { this.additionalWants = Optional.of(new TradeCost(item, JTradeSet.NumberProvider.constant(count))); return this; }
	public JVillagerTrade gives(ItemStack gives) { this.gives = gives; return this; }
	public JVillagerTrade gives(String item, int count) { return gives(new ItemStack(item, count)); }
	public JVillagerTrade maxUses(JTradeSet.NumberProvider maxUses) { this.maxUses = maxUses; return this; }
	public JVillagerTrade maxUses(int maxUses) { return maxUses(JTradeSet.NumberProvider.constant(maxUses)); }
	public JVillagerTrade reputationDiscount(JTradeSet.NumberProvider reputationDiscount) { this.reputationDiscount = reputationDiscount; return this; }
	public JVillagerTrade xp(JTradeSet.NumberProvider xp) { this.xp = xp; return this; }
	public JVillagerTrade xp(int xp) { return xp(JTradeSet.NumberProvider.constant(xp)); }
	public JVillagerTrade merchantPredicate(Optional<String> merchantPredicate) { this.merchantPredicate = merchantPredicate; return this; }
	public JVillagerTrade merchantPredicate(String merchantPredicate) { this.merchantPredicate = Optional.of(merchantPredicate); return this; }
	public JVillagerTrade givenItemModifiers(List<String> modifiers) { this.givenItemModifiers = new ArrayList<>(modifiers); return this; }
	public JVillagerTrade givenItemModifier(String modifier) { this.givenItemModifiers.add(modifier); return this; }

	public record TradeCost(String id, JTradeSet.NumberProvider count) {
		public static final Codec<TradeCost> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("id").forGetter(TradeCost::id),
				JTradeSet.NumberProvider.CODEC.optionalFieldOf("count", JTradeSet.NumberProvider.constant(1)).forGetter(TradeCost::count)
		).apply(i, TradeCost::new));
	}

	public record ItemStack(String id, int count) {
		public static final Codec<ItemStack> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("id").forGetter(ItemStack::id),
				Codec.INT.optionalFieldOf("count", 1).forGetter(ItemStack::count)
		).apply(i, ItemStack::new));
	}
}
