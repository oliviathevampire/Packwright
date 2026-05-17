package net.vampirestudios.arrp.assets.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public final class TintTeam extends Tint {
	public static final String TYPE = "minecraft:team";
	public static final Codec<TintTeam> CODEC = RecordCodecBuilder.create(i -> i.group(
			ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(TintTeam::defaultRgb)
	).apply(i, TintTeam::new));

	static {
		Tint.register(TYPE, CODEC);
	}

	private final int defaultRgb; // optional fallback RGB if no team color

	public TintTeam(int defaultRgb) {
		super(TYPE);
		this.defaultRgb = defaultRgb;
	}

	public static TintTeam of(int value) {
		return new TintTeam(value);
	}

	public int defaultRgb() {
		return defaultRgb;
	}

	@Override
	public TintTeam clone() {
		return new TintTeam(defaultRgb);
	}
}
