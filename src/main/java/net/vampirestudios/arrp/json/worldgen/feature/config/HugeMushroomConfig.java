package net.vampirestudios.arrp.json.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.json.worldgen.feature.JPlacedFeature;

public class HugeMushroomConfig implements FeatureConfig {
	public static final Codec<HugeMushroomConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			BlockStateProvider.CODEC.fieldOf("cap_provider").forGetter(x -> x.capProvider),
			BlockStateProvider.CODEC.fieldOf("stem_provider").forGetter(x -> x.stemProvider),
			Codec.INT.optionalFieldOf("foliage_radius", 2).forGetter(x -> x.foliageRadius),
			JPlacedFeature.BlockPredicate.CODEC.fieldOf("can_place_on").forGetter(x -> x.canPlaceOn)
	).apply(i, (capProvider, stemProvider, foliageRadius, canPlaceOn) -> new HugeMushroomConfig()
			.capProvider(capProvider)
			.stemProvider(stemProvider)
			.foliageRadius(foliageRadius)
			.canPlaceOn(canPlaceOn)));

	private BlockStateProvider capProvider = BlockStateProvider.simple("minecraft:red_mushroom_block");
	private BlockStateProvider stemProvider = BlockStateProvider.simple("minecraft:mushroom_stem");
	private int foliageRadius = 2;
	private JPlacedFeature.BlockPredicate canPlaceOn = JPlacedFeature.BlockPredicate.matchingBlocks("minecraft:mycelium", "minecraft:podzol");

	public static HugeMushroomConfig hugeMushroom(String capBlock, String stemBlock) {
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

	public HugeMushroomConfig canPlaceOn(JPlacedFeature.BlockPredicate canPlaceOn) {
		this.canPlaceOn = canPlaceOn;
		return this;
	}
}
