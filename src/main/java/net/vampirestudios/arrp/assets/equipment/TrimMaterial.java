package net.vampirestudios.arrp.assets.equipment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

/**
 * An element of the {@code trim_material} registry. Since 26.3 the {@code asset_name}
 * field is replaced by {@code palette} — a Palette Texture ID, i.e.
 * {@code <namespace>:<path>} resolving to {@code textures/palettes/<path>.png}
 * (vanilla trim palettes live under {@code minecraft:trim/<name>}). Per-equipment
 * overrides ({@code override_armor_assets}) moved to the resource pack's Equipment
 * Assets ({@code trim_palette_replacements}).
 */
public class TrimMaterial {
	public static final Codec<TrimMaterial> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("palette").forGetter(x -> x.palette),
			Codec.STRING.fieldOf("description").forGetter(x -> x.description)
	).apply(i, (palette, description) -> {
		TrimMaterial out = new TrimMaterial();
		out.palette = palette;
		out.description = description;
		return out;
	}));

	private Identifier palette;
	private String description;

	public static TrimMaterial trimMaterial() {
		return new TrimMaterial();
	}

	public TrimMaterial palette(Identifier palette) { this.palette = palette; return this; }
	public TrimMaterial palette(String palette) { return palette(Identifier.tryParse(palette)); }
	public TrimMaterial description(String description) { this.description = description; return this; }

	public Identifier getPalette() { return palette; }
	public String getDescription() { return description; }
}
