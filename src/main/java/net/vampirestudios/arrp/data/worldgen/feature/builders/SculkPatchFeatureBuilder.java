package net.vampirestudios.arrp.data.worldgen.feature.builders;

public final class SculkPatchFeatureBuilder extends FeatureBuilder {
	public SculkPatchFeatureBuilder() {
		super("minecraft:sculk_patch");
	}

	public SculkPatchFeatureBuilder chargeCount(int value) { feature.property("charge_count", value); return this; }
	public SculkPatchFeatureBuilder amountPerCharge(int value) { feature.property("amount_per_charge", value); return this; }
	public SculkPatchFeatureBuilder spreadAttempts(int value) { feature.property("spread_attempts", value); return this; }
	public SculkPatchFeatureBuilder growthRounds(int value) { feature.property("growth_rounds", value); return this; }
	public SculkPatchFeatureBuilder spreadRounds(int value) { feature.property("spread_rounds", value); return this; }
	public SculkPatchFeatureBuilder extraRareGrowths(float value) { feature.property("extra_rare_growths", value); return this; }
	public SculkPatchFeatureBuilder catalystChance(float value) { feature.property("catalyst_chance", value); return this; }
}
