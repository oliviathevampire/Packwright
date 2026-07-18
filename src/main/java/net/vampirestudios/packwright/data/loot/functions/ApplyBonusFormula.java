package net.vampirestudios.packwright.data.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.loot.util.LootValue;

public interface ApplyBonusFormula {
	static ApplyBonusFormula oreDrops() {
		return OreDrops.INSTANCE;
	}

	static ApplyBonusFormula uniformBonusCount(int bonusMultiplier) {
		return new UniformBonusCount(bonusMultiplier);
	}

	static ApplyBonusFormula binomialWithBonusCount(int extra, float probability) {
		return new BinomialWithBonusCount(extra, probability);
	}

	Identifier id();

	default Object parameters() {
		if (this instanceof UniformBonusCount formula) return LootValue.encode(UniformBonusCount.CODEC, formula);
		if (this instanceof BinomialWithBonusCount formula) return LootValue.encode(BinomialWithBonusCount.CODEC, formula);
		return null;
	}

	final class OreDrops implements ApplyBonusFormula {
		static final OreDrops INSTANCE = new OreDrops();

		private OreDrops() {
		}

		@Override
		public Identifier id() {
			return Identifier.withDefaultNamespace("ore_drops");
		}
	}

	record UniformBonusCount(int bonusMultiplier) implements ApplyBonusFormula {
		static final Codec<UniformBonusCount> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.INT.fieldOf("bonusMultiplier").forGetter(UniformBonusCount::bonusMultiplier)
		).apply(i, UniformBonusCount::new));

		@Override
		public Identifier id() {
			return Identifier.withDefaultNamespace("uniform_bonus_count");
		}
	}

	record BinomialWithBonusCount(int extra, float probability) implements ApplyBonusFormula {
		static final Codec<BinomialWithBonusCount> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.INT.fieldOf("extra").forGetter(BinomialWithBonusCount::extra),
				Codec.FLOAT.fieldOf("probability").forGetter(BinomialWithBonusCount::probability)
		).apply(i, BinomialWithBonusCount::new));

		@Override
		public Identifier id() {
			return Identifier.withDefaultNamespace("binomial_with_bonus_count");
		}
	}
}
