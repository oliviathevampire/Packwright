package net.vampirestudios.packwright.assets.font;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A font provider that renders nothing and just advances the cursor
 * by a fixed number of pixels per character.
 */
public final class ProviderSpace extends FontProvider {
	public static final String TYPE = "space";
	public static final Codec<ProviderSpace> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.unboundedMap(Codec.STRING, Codec.FLOAT).fieldOf("advances")
					.forGetter(p -> Map.copyOf(p.advances))
	).apply(i, advances -> {
		ProviderSpace out = new ProviderSpace();
		out.advances.putAll(advances);
		return out;
	}));

	static {
		FontProvider.register(TYPE, CODEC);
	}

	private final Map<String, Float> advances = new LinkedHashMap<>();

	public ProviderSpace() {
		super(TYPE);
	}

	/** maps a character (as a one-codepoint string) to its advance in pixels */
	public ProviderSpace advance(String character, float advance) {
		this.advances.put(character, advance);
		return this;
	}

	public Map<String, Float> getAdvances() { return advances; }
}
