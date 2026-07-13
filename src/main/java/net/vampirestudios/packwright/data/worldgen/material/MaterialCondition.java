package net.vampirestudios.packwright.data.worldgen.material;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.VerticalAnchor;

import java.util.List;

public sealed interface MaterialCondition permits
		ReferenceMaterialCondition,
		BiomeMaterialCondition,
		NotMaterialCondition,
		YAboveMaterialCondition,
		StoneDepthMaterialCondition,
		VerticalGradientMaterialCondition,
		NoiseThresholdMaterialCondition {

	/**
	 * Codec for inline, typed material-condition objects.
	 *
	 * <p>Registry references are handled separately by {@link #CODEC} and
	 * serialize as plain identifiers.</p>
	 */
	Codec<MaterialCondition> TYPED_CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<MaterialCondition, T>> decode(
				DynamicOps<T> ops,
				T input
		) {
			return ops.getMap(input).flatMap(map -> {
				String rawType = string(
						map,
						ops,
						"type",
						""
				);

				return switch (normalizeType(rawType)) {
					case "biome" ->
							MaterialCondition.decodeAsMaterialCondition(
									BiomeMaterialCondition.CODEC.codec(),
									ops,
									input
							);

					case "not" ->
							MaterialCondition.decodeAsMaterialCondition(
									NotMaterialCondition.CODEC.codec(),
									ops,
									input
							);

					case "y_above" ->
							MaterialCondition.decodeAsMaterialCondition(
									YAboveMaterialCondition.CODEC.codec(),
									ops,
									input
							);

					case "stone_depth" ->
							MaterialCondition.decodeAsMaterialCondition(
									StoneDepthMaterialCondition.CODEC.codec(),
									ops,
									input
							);

					case "vertical_gradient" ->
							MaterialCondition.decodeAsMaterialCondition(
									VerticalGradientMaterialCondition.CODEC.codec(),
									ops,
									input
							);

					case "noise_threshold" ->
							MaterialCondition.decodeAsMaterialCondition(
									NoiseThresholdMaterialCondition.CODEC.codec(),
									ops,
									input
							);

					default -> DataResult.error(
							() -> rawType.isEmpty()
									? "Material condition is missing its type"
									: "Unsupported material condition type: "
									  + rawType
					);
				};
			});
		}

		@Override
		public <T> DataResult<T> encode(
				MaterialCondition input,
				DynamicOps<T> ops,
				T prefix
		) {
			if (input instanceof ReferenceMaterialCondition) {
				return DataResult.error(
						() -> "Material-condition references must be encoded "
								+ "through MaterialCondition.CODEC"
				);
			}

			if (input instanceof BiomeMaterialCondition condition) {
				return BiomeMaterialCondition.CODEC.codec()
						.encode(condition, ops, prefix);
			}

			if (input instanceof NotMaterialCondition condition) {
				return NotMaterialCondition.CODEC.codec()
						.encode(condition, ops, prefix);
			}

			if (input instanceof YAboveMaterialCondition condition) {
				return YAboveMaterialCondition.CODEC.codec()
						.encode(condition, ops, prefix);
			}

			if (input instanceof StoneDepthMaterialCondition condition) {
				return StoneDepthMaterialCondition.CODEC.codec()
						.encode(condition, ops, prefix);
			}

			if (input instanceof VerticalGradientMaterialCondition condition) {
				return VerticalGradientMaterialCondition.CODEC.codec()
						.encode(condition, ops, prefix);
			}

			if (input instanceof NoiseThresholdMaterialCondition condition) {
				return NoiseThresholdMaterialCondition.CODEC.codec()
						.encode(condition, ops, prefix);
			}

			return DataResult.error(
					() -> "Unsupported material condition: "
							+ input.getClass().getName()
			);
		}
	};

	/**
	 * Material conditions may either be a reference to an entry in the
	 * {@code worldgen/material_condition} registry or an inline typed object.
	 */
	Codec<MaterialCondition> CODEC = Codec.either(
			Identifier.CODEC,
			TYPED_CODEC
	).xmap(
			value -> value.map(
					ReferenceMaterialCondition::new,
					condition -> condition
			),
			condition -> condition instanceof ReferenceMaterialCondition reference
					? Either.left(reference.id())
					: Either.right(condition)
	);

	/**
	 * References an entry in the {@code worldgen/material_condition} registry.
	 */
	static ReferenceMaterialCondition reference(Identifier id) {
		return new ReferenceMaterialCondition(id);
	}

	static BiomeMaterialCondition biome(Identifier... biomes) {
		return new BiomeMaterialCondition(
				List.of(biomes)
		);
	}

	static NotMaterialCondition not(MaterialCondition condition) {
		return new NotMaterialCondition(condition);
	}

	static YAboveMaterialCondition yAbove(
			VerticalAnchor anchor,
			int surfaceDepthMultiplier,
			boolean addStoneDepth
	) {
		return new YAboveMaterialCondition(
				anchor,
				surfaceDepthMultiplier,
				addStoneDepth
		);
	}

	/**
	 * Creates a stone-depth condition.
	 *
	 * @param surfaceType either {@code "floor"} or {@code "ceiling"}
	 */
	static StoneDepthMaterialCondition stoneDepth(
			int offset,
			boolean addSurfaceDepth,
			int secondaryDepthRange,
			String surfaceType
	) {
		return new StoneDepthMaterialCondition(
				offset,
				addSurfaceDepth,
				secondaryDepthRange,
				surfaceType
		);
	}

	/**
	 * Creates a stone-depth condition without a secondary depth range.
	 *
	 * @param surfaceType either {@code "floor"} or {@code "ceiling"}
	 */
	static StoneDepthMaterialCondition stoneDepth(
			int offset,
			boolean addSurfaceDepth,
			String surfaceType
	) {
		return stoneDepth(
				offset,
				addSurfaceDepth,
				0,
				surfaceType
		);
	}

	static VerticalGradientMaterialCondition verticalGradient(
			String randomName,
			VerticalAnchor trueAtAndBelow,
			VerticalAnchor falseAtAndAbove
	) {
		return new VerticalGradientMaterialCondition(
				randomName,
				trueAtAndBelow,
				falseAtAndAbove
		);
	}

	/**
	 * Tests a registered noise value against an inclusive threshold range.
	 */
	static NoiseThresholdMaterialCondition noiseThreshold(
			Identifier noise,
			double minThreshold,
			double maxThreshold
	) {
		return new NoiseThresholdMaterialCondition(
				noise,
				minThreshold,
				maxThreshold
		);
	}

	private static String normalizeType(String type) {
		int separator = type.indexOf(':');

		return separator >= 0
				? type.substring(separator + 1)
				: type;
	}

	private static <T> String string(
			MapLike<T> map,
			DynamicOps<T> ops,
			String key,
			String fallback
	) {
		T value = map.get(key);

		return value == null
				? fallback
				: ops.getStringValue(value)
				  .result()
				  .orElse(fallback);
	}

	private static <T, C extends MaterialCondition> DataResult<Pair<MaterialCondition, T>> decodeAsMaterialCondition(
			Codec<C> codec,
			DynamicOps<T> ops,
			T input
	) {
		return codec.decode(ops, input)
				.map(pair -> Pair.of(
						pair.getFirst(),
						pair.getSecond()
				));
	}
}