package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.ProcessorList;

import java.util.List;

public final class FossilFeatureBuilder extends FeatureBuilder {
	public FossilFeatureBuilder() {
		super("minecraft:fossil");
		maxEmptyCornersAllowed(0);
	}

	public FossilFeatureBuilder fossilStructures(List<Identifier> structures) {
		feature.property("fossil_structures", structures, Identifier.CODEC);
		return this;
	}

	public FossilFeatureBuilder overlayStructures(List<Identifier> structures) {
		feature.property("overlay_structures", structures, Identifier.CODEC);
		return this;
	}

	public FossilFeatureBuilder fossilProcessors(ProcessorList processors) {
		feature.property("fossil_processors", ProcessorList.CODEC, processors);
		return this;
	}

	public FossilFeatureBuilder overlayProcessors(ProcessorList processors) {
		feature.property("overlay_processors", ProcessorList.CODEC, processors);
		return this;
	}

	/** must be between 0 and 7 inclusive */
	public FossilFeatureBuilder maxEmptyCornersAllowed(int value) {
		feature.property("max_empty_corners_allowed", value);
		return this;
	}
}
