package net.vampirestudios.packwright.assets.atlas;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.vampirestudios.packwright.impl.Codecs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract base class for atlas sources ({@code assets/<namespace>/atlases/*.json}).
 *
 * @see SourceDirectory
 * @see SourceSingle
 * @see SourceFilter
 * @see SourceUnstitch
 * @see SourcePalettedPermutations
 */
public abstract class AtlasSource {
	private final String type;

	protected AtlasSource(String type) {
		this.type = type;
	}

	// Static factory methods for the vanilla source types
	public static SourceDirectory directory(String source, String prefix) {
		return new SourceDirectory(source, prefix);
	}

	public static SourceSingle single(Identifier resource) {
		return new SourceSingle(resource);
	}

	public static SourceFilter filter() {
		return new SourceFilter();
	}

	public static SourceUnstitch unstitch(Identifier resource, double divisorX, double divisorY) {
		return new SourceUnstitch(resource, divisorX, divisorY);
	}

	public static SourcePalettedPermutations palettedPermutations(Identifier paletteKey) {
		return new SourcePalettedPermutations(paletteKey);
	}

	public String getType() {
		return type;
	}

	// ---- Registry + base codec ----
	private static final Map<String, Codec<? extends AtlasSource>> REGISTRY = new ConcurrentHashMap<>();

	/** registers under both the plain name and the {@code minecraft:}-prefixed form */
	public static void register(String type, Codec<? extends AtlasSource> codec) {
		REGISTRY.put(type, codec);
		if (!type.contains(":")) {
			REGISTRY.put("minecraft:" + type, codec);
		}
	}

	public static final Codec<AtlasSource> CODEC = Codecs.tagged("type", AtlasSource::getType, REGISTRY::get);
}
