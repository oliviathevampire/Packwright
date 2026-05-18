package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public class RandomPatchConfig implements FeatureConfig {
	public static final Codec<RandomPatchConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.INT.fieldOf("tries").forGetter(x -> x.tries),
			Codec.INT.fieldOf("xz_spread").forGetter(x -> x.xzSpread),
			Codec.INT.fieldOf("y_spread").forGetter(x -> x.ySpread),
			Identifier.CODEC.fieldOf("feature").forGetter(x -> x.feature)
	).apply(i, (tries, xzSpread, ySpread, feature) -> new RandomPatchConfig()
			.tries(tries)
			.xzSpread(xzSpread)
			.ySpread(ySpread)
			.feature(feature)));

	private int tries = 64;
	private int xzSpread = 7;
	private int ySpread = 3;
	private Identifier feature;

	public static RandomPatchConfig randomPatch(Identifier feature) {
		return new RandomPatchConfig().feature(feature);
	}

	public RandomPatchConfig tries(int tries) {
		this.tries = tries;
		return this;
	}

	public RandomPatchConfig xzSpread(int xzSpread) {
		this.xzSpread = xzSpread;
		return this;
	}

	public RandomPatchConfig ySpread(int ySpread) {
		this.ySpread = ySpread;
		return this;
	}

	public RandomPatchConfig feature(Identifier feature) {
		this.feature = feature;
		return this;
	}
}
