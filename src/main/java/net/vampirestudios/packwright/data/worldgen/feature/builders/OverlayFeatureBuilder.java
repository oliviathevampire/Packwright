package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.minecraft.resources.Identifier;

import java.util.List;

public final class OverlayFeatureBuilder extends FeatureBuilder {
	public OverlayFeatureBuilder() {
		super("minecraft:overlay");
	}

	public OverlayFeatureBuilder features(Identifier... features) {
		feature.property("features", List.of(features));
		return this;
	}

	public OverlayFeatureBuilder featuresTag(Identifier tag) {
		feature.property("features", "#" + tag);
		return this;
	}
}
