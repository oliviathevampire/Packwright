package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.data.worldgen.IntProvider;

public class DiskConfig implements FeatureConfig {
	public static final Codec<DiskConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			BlockStateProvider.CODEC.fieldOf("state_provider").forGetter(x -> x.stateProvider),
			RuleTest.CODEC.fieldOf("target").forGetter(x -> x.target),
			IntProvider.CODEC.fieldOf("radius").forGetter(x -> x.radius),
			Codec.INT.fieldOf("half_height").forGetter(x -> x.halfHeight)
	).apply(i, (stateProvider, target, radius, halfHeight) -> new DiskConfig()
			.stateProvider(stateProvider)
			.target(target)
			.radius(radius)
			.halfHeight(halfHeight)));

	private BlockStateProvider stateProvider = BlockStateProvider.simple("minecraft:clay");
	private RuleTest target = RuleTest.block("minecraft:dirt");
	private IntProvider radius = IntProvider.constant(2);
	private int halfHeight = 1;

	public static DiskConfig disk(String block, String targetBlock, int radius) {
		return new DiskConfig().stateProvider(BlockStateProvider.simple(block)).target(RuleTest.block(targetBlock)).radius(IntProvider.constant(radius));
	}

	public DiskConfig stateProvider(BlockStateProvider stateProvider) {
		this.stateProvider = stateProvider;
		return this;
	}

	public DiskConfig target(RuleTest target) {
		this.target = target;
		return this;
	}

	public DiskConfig radius(IntProvider radius) {
		this.radius = radius;
		return this;
	}

	public DiskConfig halfHeight(int halfHeight) {
		this.halfHeight = halfHeight;
		return this;
	}
}
