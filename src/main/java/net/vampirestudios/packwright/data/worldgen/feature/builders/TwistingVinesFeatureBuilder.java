package net.vampirestudios.packwright.data.worldgen.feature.builders;

public final class TwistingVinesFeatureBuilder extends FeatureBuilder {
	public TwistingVinesFeatureBuilder() {
		super("minecraft:twisting_vines");
	}

	public TwistingVinesFeatureBuilder spreadWidth(int value) {
		feature.property("spread_width", value);
		return this;
	}

	public TwistingVinesFeatureBuilder spreadHeight(int value) {
		feature.property("spread_height", value);
		return this;
	}

	public TwistingVinesFeatureBuilder maxHeight(int value) {
		feature.property("max_height", value);
		return this;
	}
}
