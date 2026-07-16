package net.vampirestudios.packwright.data.worldgen.material;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/** true if the position is at or below the surface's water level (offset by {@code offset}, scaled with surface depth) */
public record WaterMaterialCondition(int offset, int surfaceDepthMultiplier, boolean addStoneDepth) implements MaterialCondition {
	public static final MapCodec<WaterMaterialCondition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:water"),
			Codec.INT.fieldOf("offset").forGetter(WaterMaterialCondition::offset),
			Codec.INT.fieldOf("surface_depth_multiplier").forGetter(WaterMaterialCondition::surfaceDepthMultiplier),
			Codec.BOOL.fieldOf("add_stone_depth").forGetter(WaterMaterialCondition::addStoneDepth)
	).apply(i, (type, offset, surfaceDepthMultiplier, addStoneDepth) ->
			new WaterMaterialCondition(offset, surfaceDepthMultiplier, addStoneDepth)));
}
