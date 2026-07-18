package net.vampirestudios.packwright.data.worldgen.feature.tree.root;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.packwright.data.worldgen.IntProvider;
import net.vampirestudios.packwright.data.worldgen.feature.BlockStateProvider;

import java.util.Optional;

public record MangroveRootPlacer(
		IntProvider trunkOffsetY,
		BlockStateProvider rootProvider,
		Optional<AboveRootPlacement> aboveRootPlacement,
		MangroveRootPlacement mangroveRootPlacement
) implements TreeRootPlacer {
	public static final MapCodec<MangroveRootPlacer> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:mangrove_root_placer"),
			IntProvider.CODEC.fieldOf("trunk_offset_y").forGetter(MangroveRootPlacer::trunkOffsetY),
			BlockStateProvider.CODEC.fieldOf("root_provider").forGetter(MangroveRootPlacer::rootProvider),
			AboveRootPlacement.CODEC.optionalFieldOf("above_root_placement").forGetter(MangroveRootPlacer::aboveRootPlacement),
			MangroveRootPlacement.CODEC.fieldOf("mangrove_root_placement").forGetter(MangroveRootPlacer::mangroveRootPlacement)
	).apply(i, (type, trunkOffsetY, rootProvider, aboveRootPlacement, mangroveRootPlacement) ->
			new MangroveRootPlacer(trunkOffsetY, rootProvider, aboveRootPlacement, mangroveRootPlacement)));

	public MangroveRootPlacer aboveRootPlacement(AboveRootPlacement value) {
		return new MangroveRootPlacer(trunkOffsetY, rootProvider, Optional.of(value), mangroveRootPlacement);
	}
}
