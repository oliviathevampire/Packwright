package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.arrp.data.worldgen.WorldgenBlockState;
import net.vampirestudios.arrp.util.VanillaIds;

import java.util.ArrayList;
import java.util.List;

import static net.vampirestudios.arrp.util.ResourceHelpers.vanillaId;

public class SpringConfig implements FeatureConfig {
	public static final Codec<SpringConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			WorldgenBlockState.CODEC.fieldOf("state").forGetter(x -> x.state),
			Codec.BOOL.fieldOf("requires_block_below").forGetter(x -> x.requiresBlockBelow),
			Codec.INT.fieldOf("rock_count").forGetter(x -> x.rockCount),
			Codec.INT.fieldOf("hole_count").forGetter(x -> x.holeCount),
			Identifier.CODEC.listOf().fieldOf("valid_blocks").forGetter(x -> x.validBlocks)
	).apply(i, (state, requiresBlockBelow, rockCount, holeCount, validBlocks) -> new SpringConfig()
			.state(state)
			.requiresBlockBelow(requiresBlockBelow)
			.rockCount(rockCount)
			.holeCount(holeCount)
			.validBlocks(validBlocks)));

	private WorldgenBlockState state = WorldgenBlockState.blockState(vanillaId("water"));
	private boolean requiresBlockBelow = true;
	private int rockCount = 4;
	private int holeCount = 1;
	private List<Identifier> validBlocks = new ArrayList<>(List.of(VanillaIds.STONE));

	public static SpringConfig spring(Identifier fluidBlock) {
		return new SpringConfig().state(WorldgenBlockState.blockState(fluidBlock));
	}

	public SpringConfig state(WorldgenBlockState state) {
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

	public SpringConfig validBlock(Identifier block) {
		this.validBlocks.add(block);
		return this;
	}

	public SpringConfig validBlocks(List<Identifier> validBlocks) {
		this.validBlocks = new ArrayList<>(validBlocks);
		return this;
	}
}
