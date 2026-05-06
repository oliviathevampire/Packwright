package net.vampirestudios.arrp.json.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class UnderwaterMagmaConfig implements FeatureConfig {
	public static final Codec<UnderwaterMagmaConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.intRange(0, 512).fieldOf("floor_search_range").forGetter(x -> x.floorSearchRange),
			Codec.intRange(0, 64).fieldOf("placement_radius_around_floor").forGetter(x -> x.placementRadiusAroundFloor),
			Codec.floatRange(0.0F, 1.0F).fieldOf("placement_probability_per_valid_position").forGetter(x -> x.placementProbabilityPerValidPosition)
	).apply(i, (floorSearchRange, placementRadiusAroundFloor, placementProbabilityPerValidPosition) -> new UnderwaterMagmaConfig()
			.floorSearchRange(floorSearchRange)
			.placementRadiusAroundFloor(placementRadiusAroundFloor)
			.placementProbabilityPerValidPosition(placementProbabilityPerValidPosition)));

	private int floorSearchRange = 5;
	private int placementRadiusAroundFloor = 1;
	private float placementProbabilityPerValidPosition = 0.5F;

	public static UnderwaterMagmaConfig underwaterMagma(int floorSearchRange, int placementRadiusAroundFloor, float probability) {
		return new UnderwaterMagmaConfig()
				.floorSearchRange(floorSearchRange)
				.placementRadiusAroundFloor(placementRadiusAroundFloor)
				.placementProbabilityPerValidPosition(probability);
	}

	public UnderwaterMagmaConfig floorSearchRange(int floorSearchRange) {
		this.floorSearchRange = floorSearchRange;
		return this;
	}

	public UnderwaterMagmaConfig placementRadiusAroundFloor(int placementRadiusAroundFloor) {
		this.placementRadiusAroundFloor = placementRadiusAroundFloor;
		return this;
	}

	public UnderwaterMagmaConfig placementProbabilityPerValidPosition(float placementProbabilityPerValidPosition) {
		this.placementProbabilityPerValidPosition = placementProbabilityPerValidPosition;
		return this;
	}
}
