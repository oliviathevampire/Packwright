package net.vampirestudios.arrp.data.worldgen.material;

import net.vampirestudios.arrp.data.worldgen.WorldgenBlockState;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record BlockMaterialRule(WorldgenBlockState resultState) implements MaterialRule {
	public static final MapCodec<BlockMaterialRule> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:block"),
			WorldgenBlockState.CODEC.fieldOf("result_state").forGetter(BlockMaterialRule::resultState)
	).apply(i, (type, resultState) -> new BlockMaterialRule(resultState)));
}
