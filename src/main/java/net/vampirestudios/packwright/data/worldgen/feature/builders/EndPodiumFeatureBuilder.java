package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.vampirestudios.packwright.data.worldgen.feature.*;

public final class EndPodiumFeatureBuilder extends FeatureBuilder {
	public EndPodiumFeatureBuilder() {
		super("minecraft:end_podium");
	}

	public EndPodiumFeatureBuilder active(boolean active) {
		feature.property("active", active);
		return this;
	}
}
