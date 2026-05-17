package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.data.worldgen.BlockState;

import java.util.ArrayList;
import java.util.List;

public class SpringConfig implements FeatureConfig {
	public static final Codec<SpringConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			BlockState.CODEC.fieldOf("state").forGetter(x -> x.state),
			Codec.BOOL.fieldOf("requires_block_below").forGetter(x -> x.requiresBlockBelow),
			Codec.INT.fieldOf("rock_count").forGetter(x -> x.rockCount),
			Codec.INT.fieldOf("hole_count").forGetter(x -> x.holeCount),
			Codec.STRING.listOf().fieldOf("valid_blocks").forGetter(x -> x.validBlocks)
	).apply(i, (state, requiresBlockBelow, rockCount, holeCount, validBlocks) -> new SpringConfig()
			.state(state)
			.requiresBlockBelow(requiresBlockBelow)
			.rockCount(rockCount)
			.holeCount(holeCount)
			.validBlocks(validBlocks)));

	private BlockState state = BlockState.blockState("minecraft:water");
	private boolean requiresBlockBelow = true;
	private int rockCount = 4;
	private int holeCount = 1;
	private List<String> validBlocks = new ArrayList<>(List.of("minecraft:stone"));

	public static SpringConfig spring(String fluidBlock) {
		return new SpringConfig().state(BlockState.blockState(fluidBlock));
	}

	public SpringConfig state(BlockState state) {
		this.state = state;
		return this;
	}

	public SpringConfig requiresBlockBelow(boolean requiresBlockBelow) {
		this.requiresBlockBelow = requiresBlockBelow;
		return this;
	}

	public SpringConfig rockCount(int rockCount) {
		this.rockCount = rockCount;
		return this;
	}

	public SpringConfig holeCount(int holeCount) {
		this.holeCount = holeCount;
		return this;
	}

	public SpringConfig validBlock(String block) {
		this.validBlocks.add(block);
		return this;
	}

	public SpringConfig validBlocks(List<String> validBlocks) {
		this.validBlocks = new ArrayList<>(validBlocks);
		return this;
	}
}
