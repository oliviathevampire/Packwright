package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vampirestudios.arrp.data.worldgen.WorldgenBlockState;

import static net.vampirestudios.arrp.util.ResourceHelpers.vanillaId;

public class HugeFungusConfig implements FeatureConfig {
	public static final Codec<HugeFungusConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			WorldgenBlockState.CODEC.fieldOf("valid_base_block").forGetter(x -> x.validBaseBlock),
			WorldgenBlockState.CODEC.fieldOf("stem_state").forGetter(x -> x.stemState),
			WorldgenBlockState.CODEC.fieldOf("hat_state").forGetter(x -> x.hatState),
			WorldgenBlockState.CODEC.fieldOf("decor_state").forGetter(x -> x.decorState),
			Codec.BOOL.fieldOf("planted").forGetter(x -> x.planted)
	).apply(i, (validBaseBlock, stemState, hatState, decorState, planted) -> new HugeFungusConfig()
			.validBaseBlock(validBaseBlock)
			.stemState(stemState)
			.hatState(hatState)
			.decorState(decorState)
			.planted(planted)));

	private WorldgenBlockState validBaseBlock = WorldgenBlockState.blockState(vanillaId("crimson_nylium"));
	private WorldgenBlockState stemState = WorldgenBlockState.blockState(vanillaId("crimson_stem"));
	private WorldgenBlockState hatState = WorldgenBlockState.blockState(vanillaId("nether_wart_block"));
	private WorldgenBlockState decorState = WorldgenBlockState.blockState(vanillaId("shroomlight"));
	private boolean planted = false;

	public HugeFungusConfig validBaseBlock(WorldgenBlockState validBaseBlock) {
		this.validBaseBlock = validBaseBlock;
		return this;
	}

	public HugeFungusConfig stemState(WorldgenBlockState stemState) {
		this.stemState = stemState;
		return this;
	}

	public HugeFungusConfig hatState(WorldgenBlockState hatState) {
		this.hatState = hatState;
		return this;
	}

	public HugeFungusConfig decorState(WorldgenBlockState decorState) {
		this.decorState = decorState;
		return this;
	}

	public HugeFungusConfig planted(boolean planted) {
		this.planted = planted;
		return this;
	}
}
