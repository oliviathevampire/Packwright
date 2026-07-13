package net.vampirestudios.packwright.assets.texture;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

/**
 * The {@code villager} section of a {@code .png.mcmeta} file; only used for
 * villager/zombie-villager profession textures. Controls whether the hat layer
 * of the base texture stays visible.
 */
public class VillagerSection {
	public static final String HAT_NONE = "none";
	public static final String HAT_PARTIAL = "partial";
	public static final String HAT_FULL = "full";

	public static final Codec<VillagerSection> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.optionalFieldOf("hat").forGetter(v -> Optional.ofNullable(v.hat))
	).apply(i, hat -> {
		VillagerSection out = new VillagerSection();
		hat.ifPresent(v -> out.hat = v);
		return out;
	}));

	private String hat;

	public static VillagerSection villager() {
		return new VillagerSection();
	}

	/** one of {@link #HAT_NONE}, {@link #HAT_PARTIAL}, {@link #HAT_FULL} */
	public VillagerSection hat(String hat) {
		this.hat = hat;
		return this;
	}

	public String getHat() { return hat; }
}
