package net.vampirestudios.arrp.json.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class SimpleTreeConfig implements FeatureConfig {
	public static final Codec<SimpleTreeConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.fieldOf("trunk_block").forGetter(x -> x.trunkBlock),
			Codec.STRING.fieldOf("foliage_block").forGetter(x -> x.foliageBlock),
			Codec.BOOL.optionalFieldOf("ignore_vines", false).forGetter(x -> x.ignoreVines)
	).apply(i, (trunkBlock, foliageBlock, ignoreVines) -> new SimpleTreeConfig()
			.trunkBlock(trunkBlock)
			.foliageBlock(foliageBlock)
			.ignoreVines(ignoreVines)));

	private String trunkBlock = "minecraft:oak_log";
	private String foliageBlock = "minecraft:oak_leaves";
	private boolean ignoreVines = false;

	public static SimpleTreeConfig tree(String trunkBlock, String foliageBlock) {
		return new SimpleTreeConfig().trunkBlock(trunkBlock).foliageBlock(foliageBlock);
	}

	public SimpleTreeConfig trunkBlock(String trunkBlock) {
		this.trunkBlock = trunkBlock;
		return this;
	}

	public SimpleTreeConfig foliageBlock(String foliageBlock) {
		this.foliageBlock = foliageBlock;
		return this;
	}

	public SimpleTreeConfig ignoreVines(boolean ignoreVines) {
		this.ignoreVines = ignoreVines;
		return this;
	}
}
