package net.vampirestudios.packwright.data.worldgen.feature.tree.root;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.data.worldgen.feature.BlockStateProvider;

public record AboveRootPlacement(BlockStateProvider aboveRootProvider, float aboveRootPlacementChance) {
	public static final Codec<AboveRootPlacement> CODEC = RecordCodecBuilder.create(i -> i.group(
			BlockStateProvider.CODEC.fieldOf("above_root_provider").forGetter(AboveRootPlacement::aboveRootProvider),
			Codec.FLOAT.fieldOf("above_root_placement_chance").forGetter(AboveRootPlacement::aboveRootPlacementChance)
	).apply(i, AboveRootPlacement::new));
}
