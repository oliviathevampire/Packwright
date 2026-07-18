package net.vampirestudios.packwright.assets.font;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A bitmap font provider: glyphs come from a PNG under
 * {@code textures/}, laid out in a grid where each row is one string of
 * {@code chars}.
 * <p>
 * note: unlike most texture references, {@code file} includes the
 * {@code .png} extension, e.g. {@code mymod:font/glyphs.png}
 */
public final class ProviderBitmap extends FontProvider {
	public static final String TYPE = "bitmap";
	public static final Codec<ProviderBitmap> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("file").forGetter(ProviderBitmap::getFile),
			Codec.INT.optionalFieldOf("height").forGetter(p -> Optional.ofNullable(p.height)),
			Codec.INT.fieldOf("ascent").forGetter(ProviderBitmap::getAscent),
			Codec.STRING.listOf().fieldOf("chars").forGetter(p -> List.copyOf(p.chars))
	).apply(i, (file, height, ascent, chars) -> {
		ProviderBitmap out = new ProviderBitmap(file, ascent);
		height.ifPresent(v -> out.height = v);
		out.chars.addAll(chars);
		return out;
	}));

	static {
		FontProvider.register(TYPE, CODEC);
	}

	private final Identifier file;
	private Integer height;
	private final int ascent;
	private final List<String> chars = new ArrayList<>();

	public ProviderBitmap(Identifier file, int ascent) {
		super(TYPE);
		this.file = file;
		this.ascent = ascent;
	}

	/** glyph height in pixels the texture is scaled to; the game defaults to 8 */
	public ProviderBitmap height(int height) {
		this.height = height;
		return this;
	}

	/** adds one row of characters; every row must have the same length */
	public ProviderBitmap row(String chars) {
		this.chars.add(chars);
		return this;
	}

	public Identifier getFile() { return file; }
	public Integer getHeight() { return height; }
	public int getAscent() { return ascent; }
	public List<String> getChars() { return chars; }
}
