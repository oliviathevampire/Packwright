package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.arrp.data.worldgen.WorldgenBlockState;
import net.vampirestudios.arrp.data.worldgen.IntProvider;

public class ReplaceSphereConfig implements FeatureConfig {
	public static final Codec<ReplaceSphereConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			WorldgenBlockState.CODEC.fieldOf("target").forGetter(x -> x.target),
			WorldgenBlockState.CODEC.fieldOf("state").forGetter(x -> x.state),
			IntProvider.CODEC.fieldOf("radius").forGetter(x -> x.radius)
	).apply(i, (target, state, radius) -> new ReplaceSphereConfig().target(target).state(state).radius(radius)));

	private WorldgenBlockState target = WorldgenBlockState.blockState(Identifier.withDefaultNamespace("netherrack"));
	private WorldgenBlockState state = WorldgenBlockState.blockState(Identifier.withDefaultNamespace("blackstone"));
	private IntProvider radius = IntProvider.constant(3);

	public static ReplaceSphereConfig replaceSphere(Identifier targetBlock, Identifier stateBlock, int radius) {
		return new ReplaceSphereConfig()
				.target(WorldgenBlockState.blockState(targetBlock))
				.state(WorldgenBlockState.blockState(stateBlock))
				.radius(IntProvider.constant(radius));
	}

	public ReplaceSphereConfig target(WorldgenBlockState target) {
		this.target = target;
		return this;
	}

	public ReplaceSphereConfig state(WorldgenBlockState state) {
		this.state = state;
		return this;
	}

	public ReplaceSphereConfig radius(IntProvider radius) {
		this.radius = radius;
		return this;
	}
}
