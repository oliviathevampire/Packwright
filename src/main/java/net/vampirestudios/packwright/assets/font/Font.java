package net.vampirestudios.packwright.assets.font;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * A font definition file ({@code assets/<namespace>/font/<name>.json}).
 * Providers are checked for glyphs in the order they are listed.
 *
 * @see #font()
 * @see FontProvider
 */
public class Font {
	public static final Codec<Font> CODEC = RecordCodecBuilder.create(i -> i.group(
			FontProvider.CODEC.listOf().fieldOf("providers").forGetter(f -> List.copyOf(f.providers))
	).apply(i, providers -> {
		Font font = new Font();
		font.providers.addAll(providers);
		return font;
	}));

	private final List<FontProvider> providers = new ArrayList<>();

	public static Font font() {
		return new Font();
	}

	public Font provider(FontProvider provider) {
		this.providers.add(provider);
		return this;
	}

	public List<FontProvider> getProviders() { return providers; }
}
