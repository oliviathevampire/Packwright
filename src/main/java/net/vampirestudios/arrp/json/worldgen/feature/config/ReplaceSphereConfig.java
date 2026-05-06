package net.vampirestudios.arrp.json.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.json.worldgen.BlockState;
import net.vampirestudios.arrp.json.worldgen.IntProvider;

public class ReplaceSphereConfig implements FeatureConfig {
	public static final Codec<ReplaceSphereConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			BlockState.CODEC.fieldOf("target").forGetter(x -> x.target),
			BlockState.CODEC.fieldOf("state").forGetter(x -> x.state),
			IntProvider.CODEC.fieldOf("radius").forGetter(x -> x.radius)
	).apply(i, (target, state, radius) -> new ReplaceSphereConfig().target(target).state(state).radius(radius)));

	private BlockState target = BlockState.blockState("minecraft:netherrack");
	private BlockState state = BlockState.blockState("minecraft:blackstone");
	private IntProvider radius = IntProvider.constant(3);

	public static ReplaceSphereConfig replaceSphere(String targetBlock, String stateBlock, int radius) {
		return new ReplaceSphereConfig()
				.target(BlockState.blockState(targetBlock))
				.state(BlockState.blockState(stateBlock))
				.radius(IntProvider.constant(radius));
	}

	public ReplaceSphereConfig target(BlockState target) {
		this.target = target;
		return this;
	}

	public ReplaceSphereConfig state(BlockState state) {
		this.state = state;
		return this;
	}

	public ReplaceSphereConfig radius(IntProvider radius) {
		this.radius = radius;
		return this;
	}
}
