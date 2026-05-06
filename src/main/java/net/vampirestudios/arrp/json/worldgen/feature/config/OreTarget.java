package net.vampirestudios.arrp.json.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.json.worldgen.BlockState;

public record OreTarget(RuleTest target, BlockState state) {
	public static final Codec<OreTarget> CODEC = RecordCodecBuilder.create(i -> i.group(
			RuleTest.CODEC.fieldOf("target").forGetter(OreTarget::target),
			BlockState.CODEC.fieldOf("state").forGetter(OreTarget::state)
	).apply(i, OreTarget::new));
}
