package net.vampirestudios.packwright.assets.atlas;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Generates recolored copies of grayscale template textures — this is how
 * vanilla creates every armor-trim texture without shipping hundreds of PNGs.
 * <p>
 * For each template in {@code textures} and each entry in {@code permutations},
 * the colors of the {@code palette_key} palette are replaced by the colors of
 * the permutation's palette, producing a sprite named
 * {@code <texture><separator><permutation-key>} ({@code separator} defaults to {@code _}).
 */
public final class SourcePalettedPermutations extends AtlasSource {
	public static final String TYPE = "paletted_permutations";

	public static final Codec<SourcePalettedPermutations> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.listOf().fieldOf("textures").forGetter(s -> List.copyOf(s.textures)),
			Identifier.CODEC.fieldOf("palette_key").forGetter(SourcePalettedPermutations::getPaletteKey),
			Codec.unboundedMap(Codec.STRING, Identifier.CODEC).fieldOf("permutations")
					.forGetter(s -> Map.copyOf(s.permutations)),
			Codec.STRING.optionalFieldOf("separator").forGetter(s -> Optional.ofNullable(s.separator))
	).apply(i, (textures, paletteKey, permutations, separator) -> {
		SourcePalettedPermutations out = new SourcePalettedPermutations(paletteKey);
		out.textures.addAll(textures);
		out.permutations.putAll(permutations);
		separator.ifPresent(v -> out.separator = v);
		return out;
	}));

	static {
		AtlasSource.register(TYPE, CODEC);
	}

	private final List<Identifier> textures = new ArrayList<>();
	private final Identifier paletteKey;
	private final Map<String, Identifier> permutations = new LinkedHashMap<>();
	private String separator;

	public SourcePalettedPermutations(Identifier paletteKey) {
		super(TYPE);
		this.paletteKey = paletteKey;
	}

	/** adds a grayscale template texture to recolor */
	public SourcePalettedPermutations texture(Identifier texture) {
		this.textures.add(texture);
		return this;
	}

	/** adds a permutation: suffix for the generated sprite name → palette texture */
	public SourcePalettedPermutations permutation(String key, Identifier palette) {
		this.permutations.put(key, palette);
		return this;
	}

	/** separator between texture name and permutation key in generated sprite names, {@code _} by default */
	public SourcePalettedPermutations separator(String separator) {
		this.separator = separator;
		return this;
	}

	public List<Identifier> getTextures() { return textures; }
	public Identifier getPaletteKey() { return paletteKey; }
	public Map<String, Identifier> getPermutations() { return permutations; }
	public String getSeparator() { return separator; }
}
