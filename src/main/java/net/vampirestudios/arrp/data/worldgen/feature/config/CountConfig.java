package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import net.vampirestudios.arrp.data.worldgen.IntProvider;

public class CountConfig implements FeatureConfig {
	public static final Codec<CountConfig> CODEC = IntProvider.CODEC.fieldOf("count").xmap(CountConfig::new, x -> x.count).codec();

	private IntProvider count = IntProvider.constant(1);

	public CountConfig() {
	}

	public CountConfig(IntProvider count) {
		this.count = count;
	}

	public static CountConfig count(int count) {
		return new CountConfig(IntProvider.constant(count));
	}

	public CountConfig count(IntProvider count) {
		this.count = count;
		return this;
	}
}
