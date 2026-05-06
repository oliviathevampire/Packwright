package net.vampirestudios.arrp.json.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.json.worldgen.BlockState;
import net.vampirestudios.arrp.json.worldgen.IntProvider;

public class DeltaConfig implements FeatureConfig {
	public static final Codec<DeltaConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			BlockState.CODEC.fieldOf("contents").forGetter(x -> x.contents),
			BlockState.CODEC.fieldOf("rim").forGetter(x -> x.rim),
			IntProvider.CODEC.fieldOf("size").forGetter(x -> x.size),
			IntProvider.CODEC.fieldOf("rim_size").forGetter(x -> x.rimSize)
	).apply(i, (contents, rim, size, rimSize) -> new DeltaConfig()
			.contents(contents)
			.rim(rim)
			.size(size)
			.rimSize(rimSize)));

	private BlockState contents = BlockState.blockState("minecraft:lava");
	private BlockState rim = BlockState.blockState("minecraft:magma_block");
	private IntProvider size = IntProvider.constant(3);
	private IntProvider rimSize = IntProvider.constant(1);

	public static DeltaConfig delta(String contents, String rim) {
		return new DeltaConfig().contents(BlockState.blockState(contents)).rim(BlockState.blockState(rim));
	}

	public DeltaConfig contents(BlockState contents) {
		this.contents = contents;
		return this;
	}

	public DeltaConfig rim(BlockState rim) {
		this.rim = rim;
		return this;
	}

	public DeltaConfig size(IntProvider size) {
		this.size = size;
		return this;
	}

	public DeltaConfig rimSize(IntProvider rimSize) {
		this.rimSize = rimSize;
		return this;
	}
}
