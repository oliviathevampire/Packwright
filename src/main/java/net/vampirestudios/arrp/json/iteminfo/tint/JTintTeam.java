package net.vampirestudios.arrp.json.iteminfo.tint;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public final class JTintTeam extends JTint {
	public static final String TYPE = "minecraft:team";
	public static final Codec<JTintTeam> CODEC = RecordCodecBuilder.create(i -> i.group(
			ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(JTintTeam::defaultRgb)
	).apply(i, JTintTeam::new));

	static {
		JTint.register(TYPE, CODEC);
	}

	private final int defaultRgb; // optional fallback RGB if no team color

	public JTintTeam(int defaultRgb) {
		super(TYPE);
		this.defaultRgb = defaultRgb;
	}

	public static JTintTeam of(int value) {
		return new JTintTeam(value);
	}

	public int defaultRgb() {
		return defaultRgb;
	}

	@Override
	public JTintTeam clone() {
		return new JTintTeam(defaultRgb);
	}
}
