package net.vampirestudios.arrp.data.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import static net.vampirestudios.arrp.util.ResourceHelpers.vanillaId;

public class NetherForestVegetationConfig implements FeatureConfig {
	public static final Codec<NetherForestVegetationConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			BlockStateProvider.CODEC.fieldOf("state_provider").forGetter(x -> x.stateProvider),
			Codec.INT.fieldOf("spread_width").forGetter(x -> x.spreadWidth),
			Codec.INT.fieldOf("spread_height").forGetter(x -> x.spreadHeight)
	).apply(i, (stateProvider, spreadWidth, spreadHeight) -> new NetherForestVegetationConfig()
			.stateProvider(stateProvider)
			.spreadWidth(spreadWidth)
			.spreadHeight(spreadHeight)));

	private BlockStateProvider stateProvider = BlockStateProvider.simple(vanillaId("warped_roots"));
	private int spreadWidth = 8;
	private int spreadHeight = 4;

	public static NetherForestVegetationConfig vegetation(Identifier block) {
		return new NetherForestVegetationConfig().stateProvider(BlockStateProvider.simple(block));
	}

	public NetherForestVegetationConfig stateProvider(BlockStateProvider stateProvider) {
		this.stateProvider = stateProvider;
		return this;
	}

	public NetherForestVegetationConfig spreadWidth(int spreadWidth) {
		this.spreadWidth = spreadWidth;
		return this;
	}

	public NetherForestVegetationConfig spreadHeight(int spreadHeight) {
		this.spreadHeight = spreadHeight;
		return this;
	}
}
