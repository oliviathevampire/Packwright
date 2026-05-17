package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.data.worldgen.IntProvider;

public class ColumnConfig implements FeatureConfig {
	public static final Codec<ColumnConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			IntProvider.CODEC.fieldOf("reach").forGetter(x -> x.reach),
			IntProvider.CODEC.fieldOf("height").forGetter(x -> x.height)
	).apply(i, (reach, height) -> new ColumnConfig().reach(reach).height(height)));

	private IntProvider reach = IntProvider.constant(1);
	private IntProvider height = IntProvider.constant(5);

	public static ColumnConfig column(int reach, int height) {
		return new ColumnConfig().reach(IntProvider.constant(reach)).height(IntProvider.constant(height));
	}

	public ColumnConfig reach(IntProvider reach) {
		this.reach = reach;
		return this;
	}

	public ColumnConfig height(IntProvider height) {
		this.height = height;
		return this;
	}
}
