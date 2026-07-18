package net.vampirestudios.packwright.data.worldgen.dimension.biomeSources;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.dimension.Utils;

/**
 * The vanilla {@code minecraft:the_end} biome source ({@code TheEndBiomeSource}). It has
 * no configurable fields in JSON — the game derives its five biomes (the_end,
 * end_highlands, end_midlands, small_end_islands, end_barrens) from fixed vanilla
 * registry keys, so the only key present is the "type" discriminator.
 */
public record TheEnd() implements BiomeSource {
	private static final Identifier TYPE = Identifier.withDefaultNamespace("the_end");

	public static final MapCodec<TheEnd> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Utils.typeCodec(TYPE).fieldOf("type").forGetter(source -> TYPE)
	).apply(instance, type -> new TheEnd()));

	public static final TheEnd INSTANCE = new TheEnd();
}
