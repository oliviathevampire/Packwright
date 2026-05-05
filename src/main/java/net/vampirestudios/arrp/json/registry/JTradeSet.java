package net.vampirestudios.arrp.json.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JTradeSet {
	public static final Codec<JTradeSet> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.listOf().fieldOf("trades").forGetter(x -> x.trades),
			NumberProvider.CODEC.fieldOf("amount").forGetter(x -> x.amount),
			Codec.BOOL.optionalFieldOf("allow_duplicates", false).forGetter(x -> x.allowDuplicates),
			Identifier.CODEC.optionalFieldOf("random_sequence").forGetter(x -> x.randomSequence)
	).apply(i, (trades, amount, allowDuplicates, randomSequence) -> new JTradeSet().trades(trades).amount(amount).allowDuplicates(allowDuplicates).randomSequence(randomSequence)));

	private List<String> trades = new ArrayList<>();
	private NumberProvider amount = NumberProvider.constant(1);
	private boolean allowDuplicates;
	private Optional<Identifier> randomSequence = Optional.empty();

	public static JTradeSet tradeSet() { return new JTradeSet(); }
	public JTradeSet trades(List<String> trades) { this.trades = new ArrayList<>(trades); return this; }
	public JTradeSet trade(String trade) { this.trades.add(trade); return this; }
	public JTradeSet amount(NumberProvider amount) { this.amount = amount; return this; }
	public JTradeSet amount(int amount) { return amount(NumberProvider.constant(amount)); }
	public JTradeSet allowDuplicates(boolean allowDuplicates) { this.allowDuplicates = allowDuplicates; return this; }
	public JTradeSet randomSequence(Optional<Identifier> randomSequence) { this.randomSequence = randomSequence; return this; }
	public JTradeSet randomSequence(Identifier randomSequence) { this.randomSequence = Optional.of(randomSequence); return this; }

	public record NumberProvider(String type, int value, int min, int max) {
		public static final Codec<NumberProvider> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.optionalFieldOf("type", "minecraft:constant").forGetter(NumberProvider::type),
				Codec.INT.optionalFieldOf("value", 0).forGetter(NumberProvider::value),
				Codec.INT.optionalFieldOf("min", 0).forGetter(NumberProvider::min),
				Codec.INT.optionalFieldOf("max", 0).forGetter(NumberProvider::max)
		).apply(i, NumberProvider::new));
		public static NumberProvider constant(int value) { return new NumberProvider("minecraft:constant", value, value, value); }
		public static NumberProvider uniform(int min, int max) { return new NumberProvider("minecraft:uniform", 0, min, max); }
	}
}
