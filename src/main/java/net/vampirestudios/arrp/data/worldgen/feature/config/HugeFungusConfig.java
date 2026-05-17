package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.data.worldgen.BlockState;

public class HugeFungusConfig implements FeatureConfig {
	public static final Codec<HugeFungusConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			BlockState.CODEC.fieldOf("valid_base_block").forGetter(x -> x.validBaseBlock),
			BlockState.CODEC.fieldOf("stem_state").forGetter(x -> x.stemState),
			BlockState.CODEC.fieldOf("hat_state").forGetter(x -> x.hatState),
			BlockState.CODEC.fieldOf("decor_state").forGetter(x -> x.decorState),
			Codec.BOOL.fieldOf("planted").forGetter(x -> x.planted)
	).apply(i, (validBaseBlock, stemState, hatState, decorState, planted) -> new HugeFungusConfig()
			.validBaseBlock(validBaseBlock)
			.stemState(stemState)
			.hatState(hatState)
			.decorState(decorState)
			.planted(planted)));

	private BlockState validBaseBlock = BlockState.blockState("minecraft:crimson_nylium");
	private BlockState stemState = BlockState.blockState("minecraft:crimson_stem");
	private BlockState hatState = BlockState.blockState("minecraft:nether_wart_block");
	private BlockState decorState = BlockState.blockState("minecraft:shroomlight");
	private boolean planted = false;

	public HugeFungusConfig validBaseBlock(BlockState validBaseBlock) {
		this.validBaseBlock = validBaseBlock;
		return this;
	}

	public HugeFungusConfig stemState(BlockState stemState) {
		this.stemState = stemState;
		return this;
	}

	public HugeFungusConfig hatState(BlockState hatState) {
		this.hatState = hatState;
		return this;
	}

	public HugeFungusConfig decorState(BlockState decorState) {
		this.decorState = decorState;
		return this;
	}

	public HugeFungusConfig planted(boolean planted) {
		this.planted = planted;
		return this;
	}
}
