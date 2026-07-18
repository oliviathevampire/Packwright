package net.vampirestudios.packwright.data.worldgen.feature.builders;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

public final class EndSpikeFeatureBuilder extends FeatureBuilder {
	private final List<Spike> spikes = new ArrayList<>();

	public EndSpikeFeatureBuilder() {
		super("minecraft:end_spike");
		this.feature.property("spikes", spikes, Spike.CODEC);
	}

	/** an empty spike list makes the game generate the vanilla ring of 10 spikes around the dragon fight */
	public EndSpikeFeatureBuilder spike(int centerX, int centerZ, int radius, int height, boolean guarded) {
		spikes.add(new Spike(centerX, centerZ, radius, height, guarded));
		this.feature.property("spikes", spikes, Spike.CODEC);
		return this;
	}

	public EndSpikeFeatureBuilder crystalInvulnerable(boolean value) {
		feature.property("crystal_invulnerable", value);
		return this;
	}

	public EndSpikeFeatureBuilder crystalBeamTarget(int x, int y, int z) {
		feature.property("crystal_beam_target", BlockPos.CODEC, new BlockPos(x, y, z));
		return this;
	}

	public record Spike(int centerX, int centerZ, int radius, int height, boolean guarded) {
		public static final Codec<Spike> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.INT.fieldOf("centerX").orElse(0).forGetter(Spike::centerX),
				Codec.INT.fieldOf("centerZ").orElse(0).forGetter(Spike::centerZ),
				Codec.INT.fieldOf("radius").orElse(0).forGetter(Spike::radius),
				Codec.INT.fieldOf("height").orElse(0).forGetter(Spike::height),
				Codec.BOOL.fieldOf("guarded").orElse(false).forGetter(Spike::guarded)
		).apply(i, Spike::new));
	}

	private record BlockPos(int x, int y, int z) {
		static final Codec<BlockPos> CODEC = Codec.INT.listOf().comapFlatMap(
				list -> list.size() == 3
						? com.mojang.serialization.DataResult.success(new BlockPos(list.get(0), list.get(1), list.get(2)))
						: com.mojang.serialization.DataResult.error(() -> "BlockPos must have 3 elements"),
				pos -> List.of(pos.x(), pos.y(), pos.z())
		);
	}
}
