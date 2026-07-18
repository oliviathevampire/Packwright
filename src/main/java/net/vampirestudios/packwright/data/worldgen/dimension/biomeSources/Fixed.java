package net.vampirestudios.packwright.data.worldgen.dimension.biomeSources;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.dimension.Utils;

import java.util.Objects;

/**
 * A biome source containing one biome everywhere.
 */
public record Fixed(Identifier biome) implements BiomeSource {
	private static final Identifier TYPE = Identifier.withDefaultNamespace("fixed");

	public static final MapCodec<Fixed> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Utils.typeCodec(TYPE).fieldOf("type").forGetter(source -> TYPE),
			Identifier.CODEC.fieldOf("biome").forGetter(Fixed::biome)
	).apply(instance, (type, biome) -> new Fixed(biome)));

	public Fixed {
		Objects.requireNonNull(biome, "biome");
	}
}