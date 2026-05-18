package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.arrp.data.worldgen.WorldgenBlockState;

import java.util.ArrayList;
import java.util.List;

public class ReplaceBlockConfig implements FeatureConfig {
	public static final Codec<ReplaceBlockConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			OreTarget.CODEC.listOf().fieldOf("targets").forGetter(x -> x.targets)
	).apply(i, targets -> new ReplaceBlockConfig().targets(targets)));

	private List<OreTarget> targets = new ArrayList<>();

	public static ReplaceBlockConfig replace(Identifier targetBlock, Identifier replaceWith) {
		return new ReplaceBlockConfig().target(RuleTest.block(targetBlock), WorldgenBlockState.blockState(replaceWith));
	}

	public ReplaceBlockConfig target(RuleTest target, WorldgenBlockState state) {
		this.targets.add(new OreTarget(target, state));
		return this;
	}

	public ReplaceBlockConfig targets(List<OreTarget> targets) {
		this.targets = new ArrayList<>(targets);
		return this;
	}
}
