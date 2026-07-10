package net.vampirestudios.packwright.data.worldgen.feature.tree.root;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.feature.BlockStateProvider;

import java.util.List;

public record MangroveRootPlacement(
		List<Identifier> canGrowThrough,
		BlockStateProvider muddyRootsIn,
		BlockStateProvider muddyRootsProvider,
		int maxRootWidth,
		int maxRootLength,
		float randomSkewChance
) {
	public static final Codec<MangroveRootPlacement> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.listOf().fieldOf("can_grow_through").forGetter(MangroveRootPlacement::canGrowThrough),
			BlockStateProvider.CODEC.fieldOf("muddy_roots_in").forGetter(MangroveRootPlacement::muddyRootsIn),
			BlockStateProvider.CODEC.fieldOf("muddy_roots_provider").forGetter(MangroveRootPlacement::muddyRootsProvider),
			Codec.INT.fieldOf("max_root_width").forGetter(MangroveRootPlacement::maxRootWidth),
			Codec.INT.fieldOf("max_root_length").forGetter(MangroveRootPlacement::maxRootLength),
			Codec.FLOAT.fieldOf("random_skew_chance").forGetter(MangroveRootPlacement::randomSkewChance)
	).apply(i, MangroveRootPlacement::new));
}
