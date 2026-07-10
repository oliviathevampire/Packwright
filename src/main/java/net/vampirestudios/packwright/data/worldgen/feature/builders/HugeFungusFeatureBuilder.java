package net.vampirestudios.packwright.data.worldgen.feature.builders;

import net.vampirestudios.packwright.data.worldgen.feature.*;

import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.WorldgenBlockState;

public final class HugeFungusFeatureBuilder extends FeatureBuilder {
	public HugeFungusFeatureBuilder() {
		super("minecraft:huge_fungus");
	}

	public HugeFungusFeatureBuilder validBaseBlock(Identifier block) { feature.property("valid_base_block", WorldgenBlockState.CODEC, WorldgenBlockState.blockState(block)); return this; }
	public HugeFungusFeatureBuilder stemState(Identifier block) { feature.property("stem_state", WorldgenBlockState.CODEC, WorldgenBlockState.blockState(block)); return this; }
	public HugeFungusFeatureBuilder hatState(Identifier block) { feature.property("hat_state", WorldgenBlockState.CODEC, WorldgenBlockState.blockState(block)); return this; }
	public HugeFungusFeatureBuilder decorState(Identifier block) { feature.property("decor_state", WorldgenBlockState.CODEC, WorldgenBlockState.blockState(block)); return this; }
	public HugeFungusFeatureBuilder planted(boolean planted) { feature.property("planted", planted); return this; }
}
