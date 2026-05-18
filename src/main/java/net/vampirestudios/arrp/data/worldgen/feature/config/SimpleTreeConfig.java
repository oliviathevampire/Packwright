package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.arrp.util.VanillaIds;

public class SimpleTreeConfig implements FeatureConfig {
	public static final Codec<SimpleTreeConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			Identifier.CODEC.fieldOf("trunk_block").forGetter(x -> x.trunkBlock),
			Identifier.CODEC.fieldOf("foliage_block").forGetter(x -> x.foliageBlock),
			Codec.BOOL.optionalFieldOf("ignore_vines", false).forGetter(x -> x.ignoreVines)
	).apply(i, (trunkBlock, foliageBlock, ignoreVines) -> new SimpleTreeConfig()
			.trunkBlock(trunkBlock)
			.foliageBlock(foliageBlock)
			.ignoreVines(ignoreVines)));

	private Identifier trunkBlock = VanillaIds.OAK_LOG;
	private Identifier foliageBlock = VanillaIds.OAK_LEAVES;
	private boolean ignoreVines = false;

	public static SimpleTreeConfig tree(Identifier trunkBlock, Identifier foliageBlock) {
		return new SimpleTreeConfig().trunkBlock(trunkBlock).foliageBlock(foliageBlock);
	}

	public SimpleTreeConfig trunkBlock(Identifier trunkBlock) {
		this.trunkBlock = trunkBlock;
		return this;
	}

	public SimpleTreeConfig foliageBlock(Identifier foliageBlock) {
		this.foliageBlock = foliageBlock;
		return this;
	}

	public SimpleTreeConfig ignoreVines(boolean ignoreVines) {
		this.ignoreVines = ignoreVines;
		return this;
	}
}
