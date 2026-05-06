package net.vampirestudios.arrp.json.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.json.worldgen.BlockState;

import java.util.ArrayList;
import java.util.List;

public class OreConfig implements FeatureConfig {
	public static final Codec<OreConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			OreTarget.CODEC.listOf().fieldOf("targets").forGetter(x -> x.targets),
			Codec.INT.fieldOf("size").forGetter(x -> x.size),
			Codec.FLOAT.optionalFieldOf("discard_chance_on_air_exposure", 0.0F).forGetter(x -> x.discardChanceOnAirExposure)
	).apply(i, (targets, size, discardChance) -> new OreConfig().targets(targets).size(size).discardChanceOnAirExposure(discardChance)));

	private List<OreTarget> targets = new ArrayList<>();
	private int size = 9;
	private float discardChanceOnAirExposure = 0.0F;

	public static OreConfig ore(String replaceableTag, String block, int size) {
		return new OreConfig().target(RuleTest.tag(replaceableTag), BlockState.blockState(block)).size(size);
	}

	public OreConfig target(RuleTest target, BlockState state) {
		this.targets.add(new OreTarget(target, state));
		return this;
	}

	public OreConfig targets(List<OreTarget> targets) {
		this.targets = new ArrayList<>(targets);
		return this;
	}

	public OreConfig size(int size) {
		this.size = size;
		return this;
	}

	public OreConfig discardChanceOnAirExposure(float discardChanceOnAirExposure) {
		this.discardChanceOnAirExposure = discardChanceOnAirExposure;
		return this;
	}
}
