package net.vampirestudios.packwright.data.worldgen.material;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * @param surfaceType {@code "floor"} or {@code "ceiling"}
 */
public record StoneDepthMaterialCondition(int offset, boolean addSurfaceDepth, int secondaryDepthRange, String surfaceType) implements MaterialCondition {
	public static final MapCodec<StoneDepthMaterialCondition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:stone_depth"),
			Codec.INT.fieldOf("offset").forGetter(StoneDepthMaterialCondition::offset),
			Codec.BOOL.fieldOf("add_surface_depth").forGetter(StoneDepthMaterialCondition::addSurfaceDepth),
			Codec.INT.fieldOf("secondary_depth_range").forGetter(StoneDepthMaterialCondition::secondaryDepthRange),
			Codec.STRING.fieldOf("surface_type").forGetter(StoneDepthMaterialCondition::surfaceType)
	).apply(i, (type, offset, addSurfaceDepth, secondaryDepthRange, surfaceType) ->
			new StoneDepthMaterialCondition(offset, addSurfaceDepth, secondaryDepthRange, surfaceType)));
}
