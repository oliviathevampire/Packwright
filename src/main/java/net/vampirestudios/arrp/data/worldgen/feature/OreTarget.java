package net.vampirestudios.arrp.data.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.data.worldgen.WorldgenBlockState;

public record OreTarget(RuleTest target, WorldgenBlockState state) {
	public static final Codec<OreTarget> CODEC = RecordCodecBuilder.create(i -> i.group(
			RuleTest.CODEC.fieldOf("target").forGetter(OreTarget::target),
			WorldgenBlockState.CODEC.fieldOf("state").forGetter(OreTarget::state)
	).apply(i, OreTarget::new));
}
