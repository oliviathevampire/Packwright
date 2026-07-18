package net.vampirestudios.packwright.assets.font;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.impl.Codecs;

import java.util.List;
import java.util.Optional;

/**
 * A TrueType/OpenType font provider. {@code file} points to a font file under
 * {@code assets/<namespace>/font/}, including the extension.
 */
public final class ProviderTtf extends FontProvider {
	public static final String TYPE = "ttf";
	public static final Codec<ProviderTtf> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("file").forGetter(ProviderTtf::getFile),
			Codec.FLOAT.listOf().optionalFieldOf("shift").forGetter(p -> Optional.ofNullable(p.shift)),
			Codec.FLOAT.optionalFieldOf("size").forGetter(p -> Optional.ofNullable(p.size)),
			Codec.FLOAT.optionalFieldOf("oversample").forGetter(p -> Optional.ofNullable(p.oversample)),
			Codecs.oneOrList(Codec.STRING).optionalFieldOf("skip").forGetter(p -> Optional.ofNullable(p.skip))
	).apply(i, (file, shift, size, oversample, skip) -> {
		ProviderTtf out = new ProviderTtf(file);
		shift.ifPresent(v -> out.shift = v);
		size.ifPresent(v -> out.size = v);
		oversample.ifPresent(v -> out.oversample = v);
		skip.ifPresent(v -> out.skip = v);
		return out;
	}));

	static {
		FontProvider.register(TYPE, CODEC);
	}

	private final Identifier file;
	private List<Float> shift;
	private Float size;
	private Float oversample;
	private List<String> skip;

	public ProviderTtf(Identifier file) {
		super(TYPE);
		this.file = file;
	}

	/** glyph offset as {@code [x, y]} */
	public ProviderTtf shift(float x, float y) {
		this.shift = List.of(x, y);
		return this;
	}

	/** glyph height in pixels; the game defaults to 11 */
	public ProviderTtf size(float size) {
		this.size = size;
		return this;
	}

	/** rasterization resolution multiplier */
	public ProviderTtf oversample(float oversample) {
		this.oversample = oversample;
		return this;
	}

	/** characters to exclude from this provider */
	public ProviderTtf skip(String... characters) {
		this.skip = List.of(characters);
		return this;
	}

	public Identifier getFile() { return file; }
	public List<Float> getShift() { return shift; }
	public Float getSize() { return size; }
	public Float getOversample() { return oversample; }
	public List<String> getSkip() { return skip; }
}
