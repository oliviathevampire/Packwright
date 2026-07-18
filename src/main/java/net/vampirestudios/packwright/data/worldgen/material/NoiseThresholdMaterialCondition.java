package net.vampirestudios.packwright.data.worldgen.material;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public record NoiseThresholdMaterialCondition(
		Identifier noise,
		double minThreshold,
		double maxThreshold,
		boolean is3d
) implements MaterialCondition {

	private static final Codec<String> TYPE_CODEC =
			Codec.STRING.comapFlatMap(
					type -> {
						String normalized = normalizeType(type);

						return normalized.equals("noise_threshold")
								? DataResult.success(type)
								: DataResult.error(
										() -> "Expected material condition type "
												+ "minecraft:noise_threshold, got "
												+ type
								);
					},
					type -> "minecraft:noise_threshold"
			);

	public static final MapCodec<NoiseThresholdMaterialCondition> CODEC =
			RecordCodecBuilder.mapCodec(instance -> instance.group(
					TYPE_CODEC
							.fieldOf("type")
							.forGetter(condition ->
									"minecraft:noise_threshold"
							),

					Identifier.CODEC
							.fieldOf("noise")
							.forGetter(
									NoiseThresholdMaterialCondition::noise
							),

					Codec.DOUBLE
							.fieldOf("min_threshold")
							.forGetter(
									NoiseThresholdMaterialCondition::minThreshold
							),

					Codec.DOUBLE
							.fieldOf("max_threshold")
							.forGetter(
									NoiseThresholdMaterialCondition::maxThreshold
							),

					Codec.BOOL
							.fieldOf("is_3d")
							.orElse(false)
							.forGetter(
									NoiseThresholdMaterialCondition::is3d
							)
			).apply(
					instance,
					(type, noise, minThreshold, maxThreshold, is3d) ->
							new NoiseThresholdMaterialCondition(
									noise,
									minThreshold,
									maxThreshold,
									is3d
							)
			));

	public NoiseThresholdMaterialCondition {
		if (minThreshold > maxThreshold) {
			throw new IllegalArgumentException(
					"Minimum noise threshold "
							+ minThreshold
							+ " exceeds maximum threshold "
							+ maxThreshold
			);
		}
	}

	private static String normalizeType(String type) {
		int separator = type.indexOf(':');

		return separator >= 0
				? type.substring(separator + 1)
				: type;
	}
}