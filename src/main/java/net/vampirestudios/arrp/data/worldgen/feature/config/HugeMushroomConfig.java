package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.arrp.data.worldgen.feature.PlacedFeature;
import net.vampirestudios.arrp.util.VanillaIds;

public class HugeMushroomConfig implements FeatureConfig {
	public static final Codec<HugeMushroomConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			BlockStateProvider.CODEC.fieldOf("cap_provider").forGetter(x -> x.capProvider),
			BlockStateProvider.CODEC.fieldOf("stem_provider").forGetter(x -> x.stemProvider),
			Codec.INT.optionalFieldOf("foliage_radius", 2).forGetter(x -> x.foliageRadius),
			PlacedFeature.BlockPredicate.CODEC.fieldOf("can_place_on").forGetter(x -> x.canPlaceOn)
	).apply(i, (capProvider, stemProvider, foliageRadius, canPlaceOn) -> new HugeMushroomConfig()
			.capProvider(capProvider)
			.stemProvider(stemProvider)
			.foliageRadius(foliageRadius)
			.canPlaceOn(canPlaceOn)));

	private BlockStateProvider capProvider = BlockStateProvider.simple(VanillaIds.RED_MUSHROOM_BLOCK);
	private BlockStateProvider stemProvider = BlockStateProvider.simple(VanillaIds.MUSHROOM_STEM);
	private int foliageRadius = 2;
	private PlacedFeature.BlockPredicate canPlaceOn = PlacedFeature.BlockPredicate.matchingBlocks(VanillaIds.MYCELIUM, VanillaIds.PODZOL);

	public static HugeMushroomConfig hugeMushroom(Identifier capBlock, Identifier stemBlock) {
		return new HugeMushroomConfig()
				.capProvider(BlockStateProvider.simple(capBlock))
				.stemProvider(BlockStateProvider.simple(stemBlock));
	}

	public HugeMushroomConfig capProvider(BlockStateProvider capProvider) {
		this.capProvider = capProvider;
		return this;
	}

	public HugeMushroomConfig stemProvider(BlockStateProvider stemProvider) {
		this.stemProvider = stemProvider;
		return this;
	}

	public HugeMushroomConfig foliageRadius(int foliageRadius) {
		this.foliageRadius = foliageRadius;
		return this;
	}

	public HugeMushroomConfig canPlaceOn(PlacedFeature.BlockPredicate canPlaceOn) {
		this.canPlaceOn = canPlaceOn;
		return this;
	}
}
