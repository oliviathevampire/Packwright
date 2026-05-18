package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.vampirestudios.arrp.data.worldgen.WorldgenBlockState;
import net.vampirestudios.arrp.data.worldgen.IntProvider;

import static net.vampirestudios.arrp.util.ResourceHelpers.vanillaId;

public class DeltaConfig implements FeatureConfig {
	public static final Codec<DeltaConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			WorldgenBlockState.CODEC.fieldOf("contents").forGetter(x -> x.contents),
			WorldgenBlockState.CODEC.fieldOf("rim").forGetter(x -> x.rim),
			IntProvider.CODEC.fieldOf("size").forGetter(x -> x.size),
			IntProvider.CODEC.fieldOf("rim_size").forGetter(x -> x.rimSize)
	).apply(i, (contents, rim, size, rimSize) -> new DeltaConfig()
			.contents(contents)
			.rim(rim)
			.size(size)
			.rimSize(rimSize)));

	private WorldgenBlockState contents = WorldgenBlockState.blockState(vanillaId("lava"));
	private WorldgenBlockState rim = WorldgenBlockState.blockState(vanillaId("magma_block"));
	private IntProvider size = IntProvider.constant(3);
	private IntProvider rimSize = IntProvider.constant(1);

	public static DeltaConfig delta(Identifier contents, Identifier rim) {
		return new DeltaConfig().contents(WorldgenBlockState.blockState(contents)).rim(WorldgenBlockState.blockState(rim));
	}

	public DeltaConfig contents(WorldgenBlockState contents) {
		this.contents = contents;
		return this;
	}

	public DeltaConfig rim(WorldgenBlockState rim) {
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
