package net.vampirestudios.packwright.assets.font;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.impl.Codecs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract base class for font glyph providers
 * ({@code assets/<namespace>/font/*.json}).
 *
 * @see ProviderBitmap
 * @see ProviderSpace
 * @see ProviderTtf
 * @see ProviderUnihex
 * @see ProviderReference
 */
public abstract class FontProvider {
	private final String type;

	protected FontProvider(String type) {
		this.type = type;
	}

	// Static factory methods for the vanilla provider types
	public static ProviderBitmap bitmap(Identifier file, int ascent) {
		return new ProviderBitmap(file, ascent);
	}

	public static ProviderSpace space() {
		return new ProviderSpace();
	}

	public static ProviderTtf ttf(Identifier file) {
		return new ProviderTtf(file);
	}

	public static ProviderUnihex unihex(Identifier hexFile) {
		return new ProviderUnihex(hexFile);
	}

	public static ProviderReference reference(Identifier id) {
		return new ProviderReference(id);
	}

	public String getType() {
		return type;
	}

	// ---- Registry + base codec ----
	private static final Map<String, Codec<? extends FontProvider>> REGISTRY = new ConcurrentHashMap<>();

	/** registers under both the plain name and the {@code minecraft:}-prefixed form */
	public static void register(String type, Codec<? extends FontProvider> codec) {
		REGISTRY.put(type, codec);
		if (!type.contains(":")) {
			REGISTRY.put("minecraft:" + type, codec);
		}
	}

	public static final Codec<FontProvider> CODEC = Codecs.tagged("type", FontProvider::getType, REGISTRY::get);
}
