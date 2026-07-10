package net.vampirestudios.packwright.data.worldgen.material;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.List;

public record BiomeMaterialCondition(List<Identifier> biomes) implements MaterialCondition {
	public static final MapCodec<BiomeMaterialCondition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("type").forGetter(x -> "minecraft:biome"),
			Identifier.CODEC.listOf().fieldOf("biome_is").forGetter(BiomeMaterialCondition::biomes)
	).apply(i, (type, biomes) -> new BiomeMaterialCondition(biomes)));
}
