package net.vampirestudios.packwright.assets.texture;

import com.mojang.serialization.Codec;
import net.vampirestudios.packwright.impl.Codecs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract base class for GUI sprite scaling modes
 * (the {@code gui.scaling} object of a {@code .png.mcmeta} file).
 *
 * @see ScalingStretch
 * @see ScalingTile
 * @see ScalingNineSlice
 */
public abstract class Scaling {
	private final String type;

	protected Scaling(String type) {
		this.type = type;
	}

	// Static factory methods for the vanilla scaling modes
	public static ScalingStretch stretch() {
		return new ScalingStretch();
	}

	public static ScalingTile tile(int width, int height) {
		return new ScalingTile(width, height);
	}

	public static ScalingNineSlice nineSlice(int width, int height, int border) {
		return new ScalingNineSlice(width, height, border);
	}

	public static ScalingNineSlice nineSlice(int width, int height, int left, int top, int right, int bottom) {
		return new ScalingNineSlice(width, height, left, top, right, bottom);
	}

	public String getType() {
		return type;
	}

	// ---- Registry + base codec ----
	private static final Map<String, Codec<? extends Scaling>> REGISTRY = new ConcurrentHashMap<>();

	/** registers under both the plain name and the {@code minecraft:}-prefixed form */
	public static void register(String type, Codec<? extends Scaling> codec) {
		REGISTRY.put(type, codec);
		if (!type.contains(":")) {
			REGISTRY.put("minecraft:" + type, codec);
		}
	}

	public static final Codec<Scaling> CODEC = Codecs.tagged("type", Scaling::getType, REGISTRY::get);
}
