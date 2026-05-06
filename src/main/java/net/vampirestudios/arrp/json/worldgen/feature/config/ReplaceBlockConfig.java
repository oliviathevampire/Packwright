package net.vampirestudios.arrp.json.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.json.worldgen.BlockState;

import java.util.ArrayList;
import java.util.List;

public class ReplaceBlockConfig implements FeatureConfig {
	public static final Codec<ReplaceBlockConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			OreTarget.CODEC.listOf().fieldOf("targets").forGetter(x -> x.targets)
	).apply(i, targets -> new ReplaceBlockConfig().targets(targets)));

	private List<OreTarget> targets = new ArrayList<>();

	public static ReplaceBlockConfig replace(String targetBlock, String replaceWith) {
		return new ReplaceBlockConfig().target(RuleTest.block(targetBlock), BlockState.blockState(replaceWith));
	}

	public ReplaceBlockConfig target(RuleTest target, BlockState state) {
		this.targets.add(new OreTarget(target, state));
		return this;
	}

	public ReplaceBlockConfig targets(List<OreTarget> targets) {
		this.targets = new ArrayList<>(targets);
		return this;
	}
}
