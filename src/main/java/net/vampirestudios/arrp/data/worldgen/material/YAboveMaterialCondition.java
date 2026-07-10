package net.vampirestudios.arrp.data.worldgen.material;

import net.vampirestudios.arrp.data.worldgen.VerticalAnchor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record YAboveMaterialCondition(VerticalAnchor anchor, int surfaceDepthMultiplier, boolean addStoneDepth) implements MaterialCondition {
	public static final MapCodec<YAboveMaterialCondition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:y_above"),
			VerticalAnchor.CODEC.fieldOf("anchor").forGetter(YAboveMaterialCondition::anchor),
			Codec.INT.fieldOf("surface_depth_multiplier").forGetter(YAboveMaterialCondition::surfaceDepthMultiplier),
			Codec.BOOL.fieldOf("add_stone_depth").forGetter(YAboveMaterialCondition::addStoneDepth)
	).apply(i, (type, anchor, surfaceDepthMultiplier, addStoneDepth) ->
			new YAboveMaterialCondition(anchor, surfaceDepthMultiplier, addStoneDepth)));
}
