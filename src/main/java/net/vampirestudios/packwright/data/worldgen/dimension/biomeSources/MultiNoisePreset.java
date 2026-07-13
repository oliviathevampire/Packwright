package net.vampirestudios.packwright.data.worldgen.dimension.biomeSources;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.data.worldgen.dimension.Utils;

import java.util.Objects;

/**
 * A preset-backed multi-noise source.
 *
 * <p>This produces:</p>
 *
 * <pre>{@code
 * {
 *   "type": "minecraft:multi_noise",
 *   "preset": "minecraft:overworld"
 * }
 * }</pre>
 */
public record MultiNoisePreset(Identifier preset) implements BiomeSource {
	private static final Identifier TYPE = Identifier.withDefaultNamespace("multi_noise");

	public static final MapCodec<MultiNoisePreset> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Utils.typeCodec(TYPE).fieldOf("type").forGetter(source -> TYPE),
			Identifier.CODEC.fieldOf("preset").forGetter(MultiNoisePreset::preset)
	).apply(instance, (type, preset) -> new MultiNoisePreset(preset)));

	public MultiNoisePreset {
		Objects.requireNonNull(preset, "preset");
	}
}