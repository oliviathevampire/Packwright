package net.vampirestudios.packwright.data.worldgen.material;

import net.vampirestudios.packwright.data.worldgen.VerticalAnchor;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import net.minecraft.resources.Identifier;

import java.util.List;

public sealed interface MaterialCondition permits
		BiomeMaterialCondition,
		NotMaterialCondition,
		YAboveMaterialCondition,
		StoneDepthMaterialCondition,
		VerticalGradientMaterialCondition {
	Codec<MaterialCondition> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<MaterialCondition, T>> decode(DynamicOps<T> ops, T input) {
			return ops.getMap(input).flatMap(map -> switch (normalizeType(string(map, ops, "type", ""))) {
				case "biome" -> BiomeMaterialCondition.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "not" -> NotMaterialCondition.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "y_above" -> YAboveMaterialCondition.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "stone_depth" -> StoneDepthMaterialCondition.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				case "vertical_gradient" -> VerticalGradientMaterialCondition.CODEC.codec().decode(ops, input).map(pair -> pair.mapFirst(x -> x));
				default -> DataResult.error(() -> "Unsupported material condition type");
			});
		}

		@Override
		public <T> DataResult<T> encode(MaterialCondition input, DynamicOps<T> ops, T prefix) {
			if (input instanceof BiomeMaterialCondition condition) return BiomeMaterialCondition.CODEC.codec().encode(condition, ops, prefix);
			if (input instanceof NotMaterialCondition condition) return NotMaterialCondition.CODEC.codec().encode(condition, ops, prefix);
			if (input instanceof YAboveMaterialCondition condition) return YAboveMaterialCondition.CODEC.codec().encode(condition, ops, prefix);
			if (input instanceof StoneDepthMaterialCondition condition) return StoneDepthMaterialCondition.CODEC.codec().encode(condition, ops, prefix);
			if (input instanceof VerticalGradientMaterialCondition condition) return VerticalGradientMaterialCondition.CODEC.codec().encode(condition, ops, prefix);
			return DataResult.error(() -> "Unsupported material condition: " + input.getClass().getSimpleName());
		}
	};

	static BiomeMaterialCondition biome(Identifier... biomes) {
		return new BiomeMaterialCondition(List.of(biomes));
	}

	static NotMaterialCondition not(MaterialCondition condition) {
		return new NotMaterialCondition(condition);
	}

	static YAboveMaterialCondition yAbove(VerticalAnchor anchor, int surfaceDepthMultiplier, boolean addStoneDepth) {
		return new YAboveMaterialCondition(anchor, surfaceDepthMultiplier, addStoneDepth);
	}

	/**
	 * @param surfaceType {@code "floor"} or {@code "ceiling"}
	 */
	static StoneDepthMaterialCondition stoneDepth(int offset, boolean addSurfaceDepth, int secondaryDepthRange, String surfaceType) {
		return new StoneDepthMaterialCondition(offset, addSurfaceDepth, secondaryDepthRange, surfaceType);
	}

	/**
	 * stone depth check without a secondary depth range
	 */
	static StoneDepthMaterialCondition stoneDepth(int offset, boolean addSurfaceDepth, String surfaceType) {
		return new StoneDepthMaterialCondition(offset, addSurfaceDepth, 0, surfaceType);
	}

	static VerticalGradientMaterialCondition verticalGradient(String randomName, VerticalAnchor trueAtAndBelow, VerticalAnchor falseAtAndAbove) {
		return new VerticalGradientMaterialCondition(randomName, trueAtAndBelow, falseAtAndAbove);
	}

	private static String normalizeType(String type) {
		int separator = type.indexOf(':');
		return separator >= 0 ? type.substring(separator + 1) : type;
	}

	private static <T> String string(MapLike<T> map, DynamicOps<T> ops, String key, String fallback) {
		T value = map.get(key);
		return value == null ? fallback : ops.getStringValue(value).result().orElse(fallback);
	}
}
