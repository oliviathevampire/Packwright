package net.vampirestudios.packwright.assets.font;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

/**
 * A GNU Unifont {@code .hex} font provider. {@code hex_file} points to a ZIP
 * of {@code .hex} files under {@code assets/<namespace>/font/}.
 */
public final class ProviderUnihex extends FontProvider {
	public static final String TYPE = "unihex";

	/** overrides the left/right empty-column trimming for a codepoint range */
	public record SizeOverride(String from, String to, int left, int right) {
		public static final Codec<SizeOverride> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.fieldOf("from").forGetter(SizeOverride::from),
				Codec.STRING.fieldOf("to").forGetter(SizeOverride::to),
				Codec.INT.fieldOf("left").forGetter(SizeOverride::left),
				Codec.INT.fieldOf("right").forGetter(SizeOverride::right)
		).apply(i, SizeOverride::new));
	}

	public static final Codec<ProviderUnihex> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("hex_file").forGetter(ProviderUnihex::getHexFile),
			SizeOverride.CODEC.listOf().optionalFieldOf("size_overrides")
					.forGetter(p -> p.sizeOverrides.isEmpty()
							? java.util.Optional.empty()
							: java.util.Optional.of(List.copyOf(p.sizeOverrides)))
	).apply(i, (hexFile, overrides) -> {
		ProviderUnihex out = new ProviderUnihex(hexFile);
		overrides.ifPresent(out.sizeOverrides::addAll);
		return out;
	}));

	static {
		FontProvider.register(TYPE, CODEC);
	}

	private final Identifier hexFile;
	private final List<SizeOverride> sizeOverrides = new ArrayList<>();

	public ProviderUnihex(Identifier hexFile) {
		super(TYPE);
		this.hexFile = hexFile;
	}

	/** {@code from}/{@code to} are one-codepoint strings bounding the range (inclusive) */
	public ProviderUnihex sizeOverride(String from, String to, int left, int right) {
		this.sizeOverrides.add(new SizeOverride(from, to, left, right));
		return this;
	}

	public Identifier getHexFile() { return hexFile; }
	public List<SizeOverride> getSizeOverrides() { return sizeOverrides; }
}
